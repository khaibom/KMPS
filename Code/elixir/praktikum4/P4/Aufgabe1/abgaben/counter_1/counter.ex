defmodule CounterWeb.Counter do
	use CounterWeb, :live_view
	def mount(params, session, socket) do
		{:ok, assign(socket, :val, 0)}
	end
	
	def render(assigns) do
		~H"""
			<div>
			<h1 class="text-4xl font-bold text-center"> The count is: <%= @val %> </h1>
			</div>
		"""
	end
end
