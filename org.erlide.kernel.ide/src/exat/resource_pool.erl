%%
%% @author SiteView
%% @copyright Dragonflow Networks, Inc.
%% @version 1.0.0
%% @title The resource pool manager
%% @doc The resource pool needs started during system initializing phase.  The resource pool module implement managing the limited system resource for different monitor type.  
%% Limiting the vary large parallel process by queue the resource request when exeeding the resourse limits.

%% when monitor request resource, the resource counter increased by one, if less than limits, assert the resource allocated
%% if exeed the limit, put it into a queue.  Once finishing monitoring, the resource counter decrease by one or taking a queue
%% item  
%% 
%% 
%% 
%% @end

-module(resource_pool).
-compile(export_all).
-include("object.hrl").

extends () -> nil .

?PATTERN(single_test_pattern) -> {?POOLNAME,get,{'_',single_test}};
?PATTERN(release_resource_pattern)-> {?POOLNAME, get, {'_',release_resource}}.

%% can NOT return a list
?EVENT(single_test_event) -> {eresye,single_test_pattern};
?EVENT(release_resource_event)-> {eresye,release_resource_pattern}.

?ACTION(start) -> [
				   {single_test_event,test_action},
				   {release_resource_event,release_resource_action}
				  ].

%% ?ACTION(start) -> {test_event,test_action}.

start()->
	Pool = object:new(?MODULE),
	object:start(Pool),
	eresye:start(?POOLNAME),
	Pool.

resource_pool(Self)->
	?SETVALUE(name,?POOLOBJ),
	eresye:start(?VALUE(name)).

resource_pool_(Self)->eresye:stop(?VALUE(name)).

request(Name,Session,RequestType) ->
	Self = ?POOLOBJ,
	ClassName = object:getClass(Name),

	Max = erlang:apply(ClassName, get_max,[]),
	ResourceType = erlang:apply(ClassName, get_resource_type,[]),

	{Counter,Queue} = ?VALUE(ResourceType),
	
	if Counter < Max -> 
%% 		   io:format("[~w:~w] ~w:~w Counter = ~w/~w, Queue= ~w, Num of running = ~w~n", [?MODULE,?LINE,ResourceType,Name,get_counter(Name),Max,queue:len(Queue),object:get_num_of_state(running)]),
		   ?SETVALUE(ResourceType,{Counter+1,Queue}),
%% 		   io:format("[~w:~w] ~w:~w Counter = ~w/~w, Queue= ~w, Num of running = ~w~n", [?MODULE,?LINE,ResourceType,Name,get_counter(Name),Max,queue:len(Queue),object:get_num_of_state(running)]),
		   object:add_fact(Name,{Session,resource_allocated});
%% 	   	   eresye:assert(log_analyzer, {?VALUE(name),Session,erlang:now(),State}),

%% 		   object:call(Name,run);
	   true -> %reach the max, add into queue, will be droped in release() once resourse released by others
		   io:format("[~w:~w] ~w:~w Counter=~w/~w,Queue=~w,running=~w,waiting=~w~n", 
					 [?MODULE,?LINE,ResourceType,Name,Counter,Max,queue:len(Queue),object:get_num_of_state(running),object:get_num_of_state(waiting)]),
%% 	   	   eresye:assert(log_analyzer, {?VALUE(name),Session,erlang:now(),State}),
		   case RequestType of
			   frequency -> ?SETVALUE(ResourceType,{Counter,queue:in(Name,Queue)});%%for frequency timeout request, put into the tail of the queue
			   refresh -> ?SETVALUE(ResourceType,{Counter,queue:in_r(Name,Queue)}) %%for refresh request, put into the head of the queue
		   end
	end.

refresh_request(Name) -> 
	eresye:assert(?POOLNAME, {Name,request_resource,refresh}).

release(Name,Session) -> 
	Self = ?POOLOBJ,
	ClassName = object:getClass(Name),

	ResourceType = erlang:apply(ClassName, get_resource_type,[]),
	{Counter,Queue} = ?VALUE(ResourceType),
	Len = queue:len(Queue) ,
	if Len == 0 ->
%% 		  io:format("[~w:~w] ~w-4 Counter=~w,Class=~w,Queue=~w,running=~w,waiting=~w,wait for res=~w~n", 
%% 					[?MODULE,?LINE,Name,Counter,ResourceType,queue:len(Queue),object:get_num_of_state(running),object:get_num_of_state(waiting),object:get_num_of_state(waiting_for_resource)]),
		  		   
		  ?SETVALUE(ResourceType,{Counter-1,Queue});
	   true -> %get the next item to run and drop it from the queue
		  NextName = queue:get(Queue),
%% 		  io:format("[~w:~w] ~w-4 Counter=~w,Class=~w,Next=~w,Queue=~w,running=~w,waiting=~w,wait for res~w~n", 
%% 					[?MODULE,?LINE,Name,Counter,ResourceType,NextName,queue:len(Queue),object:get_num_of_state(running),object:get_num_of_state(waiting),object:get_num_of_state(waiting_for_resource)]),		   
		  ?SETVALUE(ResourceType,{Counter,queue:drop(Queue)}),
		  object:add_fact(NextName,{Session,resource_allocated})
		  %%TODO: is run time out, should still release the resource
	end.

%% register the object's resource type, the same type only once.
%% called in object:new
register(ResourceType) -> 	
	Self = ?POOLOBJ, 
	IsAttribute = object:isAttribute(Self,ResourceType),
	if IsAttribute -> ok;
	   true -> ?SETVALUE(ResourceType,{0,queue:new()})
	end.

get_counter(Name) -> 
	IsValidName = object:isValidName(Name) ,
	if IsValidName->
			IsAttribute = object:isAttribute(?POOLOBJ,object:getClass(Name)),
			if IsAttribute -> {Counter,_} = object:get(?POOLOBJ,object:getClass(Name)), Counter;
		   		true -> 0
			end;
	   true -> 0
	end.
	

set_counter(Name,Value) -> 
	ResourceType = object:getClass(Name),
	{_,Queue} = object:get(?POOLOBJ,ResourceType),	
	object:set(?POOLOBJ,ResourceType,{Value,Queue}).

get_queue_length(Name) -> 
	IsValidName = object:isValidName(Name) ,
	if IsValidName->
			IsAttribute = object:isAttribute(?POOLOBJ,object:getClass(Name)),
			if IsAttribute -> {_,Queue} = object:get(?POOLOBJ,object:getClass(Name)), queue:len(Queue);
		   		true -> 0
			end;
	   true -> 0
	end.

get_pools() ->
	PoolList = object:get_defined_attrs(?POOLOBJ),
	[{ResourceType,Counter,erlang:apply(ResourceType, get_max,[]),queue:to_list(Queue)}
			||{ResourceType,{Counter,Queue}}<-PoolList].

%%TODO: check for error	
%% 	if Len == 0 andalso Counter > Max -> % this should never happen, serious error.
%% 		  io:format("[~w:~w] Resource pool SYSTEM ERROR. Queue empty and reach the max limit ~w", [?MODULE,?LINE,{Max,Counter}]);
%% 	   true -> ok
%% 	end,

%%TODO:stat info of the resource pool: number of pool, max, queue max/size/avg, create a monitor for it
%%TODO: create a monitors to monitor the health condition of pool

%% test() ->
%% 	start().