%% ---
%% dbcs_machine
%%
%%---
-module(nnm_dbcs_node).
-compile(export_all).
-define(Table,"nnm_node").
-include("../../core/include/dbcs_common.hrl").


-define(LIST_KEY,[id,app_,status,vendor,ios_image,ios_version,machine_type,
				contact,location,object_subtype,sys_objectid,city,department,comments]).
				
-define(DETAIL_KEY,[id,caption,app_,status,ip_address,dynamic_ip,unmanaged,unmanaged_from,
					unmanaged_util,machine_type,dns,sys_name,description,vendor,systemup_time,location,
					contact,vendor_icon,ios_image,ios_version,group_status,status_description,status_led,
					city,department,comments]).
				
-define(PERFORM_KEY,[id,app_,last_boot,systemup_time,response_time,percent_loss,avg_response_time,
					min_response_time,max_response_time,cpu_load,total_memory,memory_used,percent_memory_used]).


-define(UPDATE_MUST_KEY,[id]).

-define(CREATE_MUST_KEY,[ip_address,agent_port,snmp_version,poll_interval]).

db_to_list(Data)->
	F = fun(X)->
		{X,proplists:get_value(X,Data)}
	end,
	[F(X)||X<-?LIST_KEY].
	
db_to_list(Data,Fileds)->
	F = fun(X)->
		{X,proplists:get_value(X,Data)}
	end,
	[F(X)||X<-Fileds].

get_node(Id)when is_atom(Id)->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			[{X,proplists:get_value(X,N)}||X<-?DETAIL_KEY]
	end;
get_node(_)->{error,parameter_error}.



get_node(Id,Fields)when is_atom(Id) andalso is_list(Fields)->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			[{X,proplists:get_value(X,N)}||X<-Fields]
	end;
get_node(_,_)->{error,parameter_error}.


get_preform(Id) when is_atom(Id)->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			[{X,proplists:get_value(X,N)}||X<-?PERFORM_KEY]
	end;
get_preform(_)->{error,parameter_error}.

get_value(Id,Key)when is_atom(Id) ->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			proplists:get_value(Key,N)
	end;
get_value(_,_)->{error,parameter_error}.

remove_node(Id) when is_atom(Id)->
	erlcmdb:delete_ci(?Table,Id);
remove_node(_)->{error,parameter_error}.

create_node(Dyn)when is_list(Dyn) ->
	case verify_data(Dyn,?CREATE_MUST_KEY) of
		{error,Err}->
			{error,Err};
		_->
			erlcmdb:create_ci(?Table,Dyn)
	end;
create_node(_)->{error,parameter_error}.

update_node(Dyn) when is_list(Dyn)->
	case verify_data(Dyn,?UPDATE_MUST_KEY) of
		{error,Err}->
			{error,Err};
		_->
			Id = proplists:get_value(id,Dyn),
			erlcmdb:update_ci(?Table,Id,Dyn)
	end;
update_node(_)->{error,parameter_error}.


verify_data(_,[])->{ok,verify_ok};
verify_data(Data,[K|T])->
	case proplists:get_value(K,Data) of
		undefined->
			{error,{not_contains,K}};
		_->
			verify_data(Data,T)
	end.


get_all()->
	Ret = erlcmdb:find_ci(?Table,""),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X) || X <- Ret]		
	end.
	
get_list(Fileds)->
	Ret = erlcmdb:find_ci(?Table,""),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X,Fileds) || X <- Ret]		
	end.
	
find(Fields,Where,From,Count,Order)->
	Ret = erlcmdb:find_ci(?Table,Where,From,Count,Order),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X,Fields) || X <- Ret]		
	end.

	
