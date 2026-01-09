package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"time"

	"github.com/gorilla/websocket"
)

var logFile *os.File

func initLogFile() {
	os.MkdirAll("logs", 0755)

	timestamp := time.Now().Format("2006-01-02_15-04-05")
	filename := filepath.Join("logs", fmt.Sprintf("server_%s.log", timestamp))

	var err error
	logFile, err = os.Create(filename)
	if err != nil {
		log.Printf("Warning: Could not create log file: %v", err)
		return
	}

	fmt.Printf("Server log will be saved to: %s\n", filename)
}

type Ticket struct {
	TicketID   int    `json:"ticket_id"`
	AssignedTo string `json:"assigned_to"`
}

type Client struct {
	conn     *websocket.Conn
	send     chan []byte
	clientID string
}

type Hub struct {
	clients      map[*Client]bool
	tickets      []Ticket
	commands     chan interface{}
	nextTicketID int
}

type RegisterCmd struct {
	client   *Client
	clientID string
}
type UnregisterCmd struct {
	client *Client
}

type AssignCmd struct {
	clientID string
	ticketID int
}

type CreateTicketCmd struct{}

type ClientMessage struct {
	MessageType string `json:"message_type"`
	ClientID    string `json:"client_id"`
	TicketID    int    `json:"ticket_id,omitempty"`
}

type ServerMessage struct {
	MessageType string   `json:"message_type"`
	Tickets     []Ticket `json:"tickets,omitempty"`
}

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool { return true },
}

func NewHub() *Hub {
	return &Hub{
		clients:      make(map[*Client]bool),
		tickets:      []Ticket{},
		commands:     make(chan interface{}, 100),
		nextTicketID: 1,
	}
}

func (h *Hub) Run() {
	logServer("Hub started")
	for cmd := range h.commands {
		switch c := cmd.(type) {
		case RegisterCmd:
			h.clients[c.client] = true
			c.client.clientID = c.clientID
			logServer(fmt.Sprintf("Client registered: %s", c.clientID))
			h.sendState(c.client)

		case UnregisterCmd:
			if _, ok := h.clients[c.client]; ok {
				delete(h.clients, c.client)
				close(c.client.send)
				logServer(fmt.Sprintf("Client unregistered: %s", c.client.clientID))
			}

		case AssignCmd:
			for i := range h.tickets {
				if h.tickets[i].TicketID == c.ticketID && h.tickets[i].AssignedTo == "" {
					h.tickets[i].AssignedTo = c.clientID
					logServer(fmt.Sprintf("Ticket %d assigned to %s", c.ticketID, c.clientID))
					h.broadcast()
					break
				}
			}

		case CreateTicketCmd:
			ticket := Ticket{TicketID: h.nextTicketID, AssignedTo: ""}
			h.tickets = append(h.tickets, ticket)
			logServer(fmt.Sprintf("New ticket created: %d", h.nextTicketID))
			h.nextTicketID++
			h.broadcast()
		}
	}
}

func (h *Hub) broadcast() {
	msg := ServerMessage{MessageType: "state", Tickets: h.tickets}
	data, _ := json.Marshal(msg)
	for client := range h.clients {
		select {
		case client.send <- data:
		default:
			h.commands <- UnregisterCmd{client: client}
		}
	}
	logServer(fmt.Sprintf("State broadcasted to %d client(s)", len(h.clients)))
}

func (h *Hub) sendState(client *Client) {
	msg := ServerMessage{MessageType: "state", Tickets: h.tickets}
	data, _ := json.Marshal(msg)
	client.send <- data
}

func (h *Hub) handleWS(w http.ResponseWriter, r *http.Request) {
	conn, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		logServer(fmt.Sprintf("Upgrade error: %v", err))
		return
	}

	client := &Client{conn: conn, send: make(chan []byte, 256)}

	go func() {
		defer conn.Close()
		for msg := range client.send {
			if err := conn.WriteMessage(websocket.TextMessage, msg); err != nil {
				return
			}
		}
	}()

	go func() {
		defer func() {
			h.commands <- UnregisterCmd{client: client}
			conn.Close()
		}()
		for {
			_, message, err := conn.ReadMessage()
			if err != nil {
				return
			}
			var msg ClientMessage
			if err := json.Unmarshal(message, &msg); err != nil {
				continue
			}
			switch msg.MessageType {
			case "register":
				h.commands <- RegisterCmd{client: client, clientID: msg.ClientID}
			case "assign_ticket":
				h.commands <- AssignCmd{clientID: msg.ClientID, ticketID: msg.TicketID}
			}
		}
	}()

	logServer("New WebSocket connection")
}

func listenStdin(hub *Hub) {
	logServer("Press 'n' to create a new ticket")
	reader := bufio.NewReader(os.Stdin)
	for {
		char, _ := reader.ReadByte()
		if char == 'n' || char == 'N' {
			hub.commands <- CreateTicketCmd{}
		}
	}
}

func logServer(msg string) {
	now := time.Now()
	timestamp := fmt.Sprintf("%02d:%02d:%02d.%03d",
		now.Hour(), now.Minute(), now.Second(), now.Nanosecond()/1000000)
	logMsg := fmt.Sprintf("%s [SERVER] %s\n", timestamp, msg)
	fmt.Print(logMsg)

	if logFile != nil {
		logFile.WriteString(logMsg)
		logFile.Sync()
	}
}

func main() {
	initLogFile()
	if logFile != nil {
		defer logFile.Close()
	}

	logServer("Starting KMPS Ticket System Server")

	hub := NewHub()
	go hub.Run()
	go listenStdin(hub)

	http.HandleFunc("/ws", hub.handleWS)

	logServer("Server listening on http://localhost:8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}
