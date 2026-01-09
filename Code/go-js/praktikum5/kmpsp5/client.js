let ws = null;
let clientId = '';
let logMessages = [];

function log(msg) {
    const now = new Date();
    const timestamp = String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0') + '.' +
        String(now.getMilliseconds()).padStart(3, '0');
    const logMsg = `${timestamp} [CLIENT ${clientId || 'unknown'}] ${msg}`;
    console.log(logMsg);
    logMessages.push(logMsg);
}

function connect() {
    const input = document.getElementById('clientId').value.trim();
    if (!input) {
        alert('Please enter a client ID');
        return;
    }
    
    if (ws && ws.readyState === WebSocket.OPEN) {
        log('Already connected');
        return;
    }
    
    clientId = input;
    const wsUrl = 'ws://localhost:8080/ws';
    log(`Connecting to ${wsUrl} as ${clientId}`);
    
    ws = new WebSocket(wsUrl);
    ws.onopen = () => {
        log('WebSocket connection opened');
        updateStatus('Connected', 'green');
        const registerMsg = {
            message_type: 'register',
            client_id: clientId
        };
        ws.send(JSON.stringify(registerMsg));
        log(`Sent register message: client_id=${clientId}`);
    };
    
    ws.onmessage = (event) => {
        log(`Received message: ${event.data}`);
        try {
            const msg = JSON.parse(event.data);
            if (msg.message_type === 'state') {
                log(`State update received: ${msg.tickets.length} tickets`);
                displayTickets(msg.tickets);
            }
        } catch (error) {
            log(`Error parsing message: ${error.message}`);
        }
    };
    
    ws.onerror = (error) => {
        log('WebSocket error occurred');
        console.error(error);
    };
    
    ws.onclose = () => {
        log('WebSocket connection closed');
        updateStatus('Disconnected', 'red');
        ws = null;
    };
}

function displayTickets(tickets) {
    const ticketsDiv = document.getElementById('tickets');
    
    if (tickets.length === 0) {
        ticketsDiv.textContent = 'No tickets available';
        log('No tickets to display');
        return;
    }
    
    let output = 'Ticket ID | Assigned To\n';
    output += '----------+-------------\n';
    
    tickets.forEach(ticket => {
        const assignedTo = ticket.assigned_to || '(unassigned)';
        output += `${String(ticket.ticket_id).padStart(9)} | ${assignedTo}\n`;
        log(`Ticket ${ticket.ticket_id}: ${assignedTo}`);
    });
    
    ticketsDiv.textContent = output;
}

function assignTicket() {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        alert('Not connected to server');
        log('Cannot assign ticket: not connected');
        return;
    }
    
    const ticketNumInput = document.getElementById('ticketNum').value;
    const ticketNum = parseInt(ticketNumInput);
    
    if (isNaN(ticketNum) || ticketNum < 1) {
        alert('Please enter a valid ticket number (positive integer)');
        log(`Invalid ticket number entered: ${ticketNumInput}`);
        return;
    }
    
    const assignMsg = {
        message_type: 'assign_ticket',
        client_id: clientId,
        ticket_id: ticketNum
    };
    
    ws.send(JSON.stringify(assignMsg));
    log(`Sent assign_ticket message: client_id=${clientId}, ticket_id=${ticketNum}`);
    
    document.getElementById('ticketNum').value = '';
}

function updateStatus(text, color) {
    const statusSpan = document.getElementById('status');
    statusSpan.textContent = text;
    statusSpan.style.color = color;
}

function downloadLog() {
    const logContent = logMessages.join('\n');
    const blob = new Blob([logContent], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');

    a.href = url;
    a.download = `client_${clientId}.log`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
    
    log(`Log file downloaded: client_${clientId}.log`);
}

log('Please enter your client ID and click Connect');

