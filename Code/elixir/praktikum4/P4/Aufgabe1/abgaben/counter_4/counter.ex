defmodule CounterWeb.Counter do
	use CounterWeb, :live_view
	alias Counter.Presence

	@topic "counter_increase"
	@presence_topic "presence"
	def mount(params, session, socket) do
		initial_present =
			if connected?(socket) do
				CounterWeb.Endpoint.subscribe(@topic)
				Presence.track(self(), @presence_topic, socket.id, %{})
				CounterWeb.Endpoint.subscribe(@presence_topic)
				Presence.list(@presence_topic) |> map_size
			else
				0		
			end
		{:ok, assign(socket, val: 0, present: initial_present) }
	end
	
	def render(assigns) do
		~H"""
			<div>
			<h1 class="text-4xl font-bold text-center"> The count is: <%= @val %> </h1>
			<p class="text-center">
				<.button phx-click="add-1"> +1 </.button>
				<.button phx-click="add-5"> +5 </.button>
				<.button phx-click="add-10"> +10 </.button>
			</p>
			<h1 class="text-center pt-2 text-xl">Connected Clients: <%= @present %></h1>
			</div>
		"""
	end
	def handle_event("add-1", _, socket) do
		new_state = update(socket, :val, fn val -> val + 1 end)
		CounterWeb.Endpoint.broadcast_from(self(), @topic,
						"counter_increase_event",
						new_state.assigns)		
		{:noreply, new_state}
	end
	def handle_event("add-5", _, socket) do
		new_state = update(socket, :val, fn val -> val + 5 end)
		CounterWeb.Endpoint.broadcast_from(self(), @topic,
						"counter_increase_event",
						new_state.assigns)		
		{:noreply, new_state}
	end
	def handle_event("add-10", _, socket) do
		new_state = update(socket, :val, fn val -> val + 10 end)
		CounterWeb.Endpoint.broadcast_from(self(), @topic,
						"counter_increase_event",
						new_state.assigns)		
		{:noreply, new_state}
	end
	def handle_info( %{topic: @topic, payload: payload}, socket ) do
		{:noreply, assign(socket, val: payload.val)}
	end
	def handle_info(
		%{event: "presence_diff", payload: %{joins: joins, leaves: leaves}},
		%{assigns: %{present: present}} = socket
		) do
		{_, joins} = Map.pop(joins, socket.id, %{})
		new_present = present + map_size(joins) - map_size(leaves)
		{:noreply, assign(socket, :present, new_present)}
	end
end
