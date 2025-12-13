defmodule TestateappWeb.Testateapp do
	use TestateappWeb, :live_view
        
	@topic "students_updates"	
	def mount(params, session, socket) do
		
		if connected?(socket) do
			TestateappWeb.Endpoint.subscribe(@topic)
		end

		
		    studenten = [
				 %{id: 1, name: "Heinz Faßbender", p1: 0, p2: 0, p3: 0, p4: 0, p5: 0},
				 %{id: 2, name: "Andreas Claßen", p1: 0, p2: 0, p3: 0, p4: 0, p5: 0},
				 %{id: 3, name: "StudentX", p1: 0, p2: 0, p3: 0, p4: 0, p5: 0}
				 ]
		{:ok, assign(socket, studenten: studenten)}
	end
	def render(assigns) do
		~H"""
		<div>
		<table border="1">
  			<thead>
    				<tr>
      				<th>Name</th>
      				<th>P1</th>
      				<th>P2</th>
			        <th>P3</th>
      				<th>P4</th>
     			        <th>P5</th>
   				</tr>
			  </thead>

 		 <tbody>
    		 <%= for student <- @studenten do %>
     			 <tr>
  			 <td><%= student.name %></td> 
 			  <%= for key <- [:p1, :p2, :p3, :p4, :p5] do %>
				<td>
              			<input
                		type="checkbox"
                		phx-click="toggle_one"
				phx-value-id={student.id}
				phx-value-key={Atom.to_string(key)}
                		checked={Map.get(student, key) == 1}/>
            			</td>
	        	  <% end %> 
     			  </tr>
   		 <% end %>
 		 </tbody>
		</table>
		</div>
		"""
	end

	def handle_event("toggle_one", %{"id" => id_str, "key" => key_str}, socket) do
	
		id = String.to_integer(id_str)
		key = String.to_atom(key_str)

		new_studenten =
			Enum.map(socket.assigns.studenten, fn student ->
			if student.id == id do
				new_val = if Map.get(student, key) == 1, do: 0, else: 1
				Map.put(student, key, new_val)
			else
				student
			end
			end)
	 
	 TestateappWeb.Endpoint.broadcast_from(self(), @topic, "students_updated",
		 %{studenten: new_studenten})


  	{:noreply, assign(socket, studenten: new_studenten)}
	end
	
	def handle_info( %{topic: @topic, event: "students_updated",  payload: payload},
			 socket ) do
		{:noreply, assign(socket, studenten: payload.studenten)}
	end
end
