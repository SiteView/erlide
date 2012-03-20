%% ---
%% node interface database module
%%
%%---
-module(nnm_dbcs_interface).
-compile(export_all).
-define(Table,"nnm_interface").
-include("../../core/include/dbcs_common.hrl").

-define(LIST_KEY,[id,app_,interface_name,interface_index,interface_type,interface_type_name,admin_status,
				oper_status,status,status_led,out_bps,in_bps,out_percent_util,in_percent_util]).
				
-define(DETAIL_KEY,[id,app_,status,status_led,interface_name,interface_index,interface_type,interface_type_name,interface_alias,
					interface_type_description,interface_speed,interface_mtu,interface_last_change,
					physical_address,unmanaged,unmanaged_from,unmanaged_util,admin_status,oper_status,
					inbandwidth,outbandwidth,caption,fullname,admin_status_led,oper_status_led,interface_icon,
					custom_bandwidth,severity,unpluggable,carrier_name,comments]).
				
-define(PERFORM_KEY,[id,app_,out_bps,in_bps,out_percent_util,in_percent_util,out_pps,
					in_pps,in_pkt_size,out_pkt_size,out_ucast_pps,out_mcast_pps,in_ucast_pps,
					in_mcast_pps,in_discards_thishour,in_discards_today,in_errors_thishour,in_errors_today,
					out_discards_thishour,out_discards_today,out_errors_thishour,out_errors_today,max_in_bps_today,
					max_in_bps_time,max_out_bps_today,max_out_bps_time,signal_noise,min_signal_noise,
					max_signal_noise,avg_signal_noise,total_codewords,percent_codewords_unerrored,percent_codewords_uncorrected,
					percent_codewords_uncorrectable,codewords_unerrored,codewords_uncorrected,codewords_uncorrectable
					]).
					
-define(UPDATE_MUST_KEY,[id]).

-define(CREATE_MUST_KEY,[node_id,interface_index,poll_interval]).


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

get_interface(Id)when is_atom(Id)->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			[{X,proplists:get_value(X,N)}||X<-?DETAIL_KEY]
	end;
get_interface(_)->{error,parameter_error}.


get_interface(Id,Fields)when is_atom(Id) andalso is_list(Fields)->
	case erlcmdb:find_ci(?Table,Id) of
		[]->
			{error,not_found};
		[N|_]->
			[{X,proplists:get_value(X,N)}||X<-Fields]
	end;
get_interface(_,_)->{error,parameter_error}.


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

remove_interface(Id) when is_atom(Id)->
	erlcmdb:delete_ci(?Table,Id);
remove_interface(_)->{error,parameter_error}.

create_interface(Dyn)when is_list(Dyn) ->
	case verify_data(Dyn,?CREATE_MUST_KEY) of
		{error,Err}->
			{error,Err};
		_->
			erlcmdb:create_ci(?Table,Dyn)
	end;
create_interface(_)->{error,parameter_error}.

update_interface(Dyn) when is_list(Dyn)->
	case verify_data(Dyn,?UPDATE_MUST_KEY) of
		{error,Err}->
			{error,Err};
		_->
			Id = proplists:get_value(id,Dyn),
			erlcmdb:update_ci(?Table,Id,Dyn)
	end;
update_interface(_)->{error,parameter_error}.

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
	
get_by_node(NodeId) when is_atom(NodeId)->
	Ret = erlcmdb:find_ci(?Table,["node_id=" ++ atom_to_list(NodeId)]),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X) || X <- Ret]		
	end;
get_by_node(_)->{error,parameter_error}.


get_by_node(NodeId,Fields) when is_atom(NodeId) andalso is_list(Fields)->
	Ret = erlcmdb:find_ci(?Table,["node_id=" ++ atom_to_list(NodeId)]),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X,Fields) || X <- Ret]
	end;
get_by_node(_,_)->{error,parameter_error}.


find(Fields,Where,From,Count,Order)->
	Ret = erlcmdb:find_ci(?Table,Where,From,Count,Order),
	case is_list(Ret) of
		false ->
			Ret;
		true ->
			[db_to_list(X,Fields) || X <- Ret]		
	end.
	
