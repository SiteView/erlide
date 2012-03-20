%% 
%% @doc api of nnm interface
%% @version{1.0}
%% @copyright 2010 dragonflow.com
%% @author Shi xianfang <xianfang.shi@dragonflow.com>

-module(nnm_api_interface).
-compile(export_all).


-export([get_list/0,create_interface/1,get_detail/2,update_interface/1,delete_interface/1,find/5]).

%% @spec get_list()-> ({error,Reason}|Result)
%% where
%%		Reason = atom()
%%		Result = [[{key,value}]]
%% @doc get list of interface
%%
get_list()->
	nnm_dbcs_interface:get_all().
	

%% @spec get_detail(Id)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get detail information of interface
%%
get_detail(Id)->
	nnm_dbcs_interface:get_interface(Id).
	

%% @spec get_detail(Id,Fileds)-> ({error,Reason}|Result)
%% where
%%		Fileds = list()
%%		Id = atom()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get detail information of interface
%%
get_detail(Id,Fields)->
	nnm_dbcs_interface:get_interface(Id,Fields).

%% @spec get_perform(Id)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get performate information of interface
%%
get_perform(Id)->
	nnm_dbcs_interface:get_perform(Id).

%% @spec get_interface_value(Id,Key)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Key = atom()
%%		Reason = atom()
%%		Result = term()
%% @doc get a single value of interface
%%
get_interface_value(Id,Key)->
	nnm_dbcs_interface:get_value(Id,Key).
	
%% @spec create_interface(Interface)-> ({error,Reason}|{ok,Result})
%% where
%%		Interface = [{key,value}]
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc create a new interface
%%
create_interface(Interface)->
	case nnm_dbcs_interface:create_interface(Interface) of
		{ok,Data}->
			Id = proplists:get_value(id,Data),
			case nnm_poller_loader:schedule_interface(Id) of
				{ok,_}->
					{ok,Data};
				Else2->
					Else2
			end;
		Else->
			Else
	end.

%% @spec update_interface(Interface)-> ({error,Reason}|{ok,Result})
%% where
%%		Interface = [{key,value}]
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc create a new interface
%%
update_interface(Interface)->
	case nnm_dbcs_interface:update_interface(Interface) of
		{ok,Data}->
			Id = proplists:get_value(id,Data),
			case nnm_poller_loader:unschedule_interface(Id) of
				{ok,_}->
					case nnm_poller_loader:schedule_interface(Id) of
						{ok,_}->
							{ok,Data};
						Else2->
							Else2
					end;
				Else3->
					Else3
			end;
		Else->
			Else
	end.
	
%% @spec delete_interface(Id)-> ({error,Reason}|{ok,Result})
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc delete a interface by id
%%
delete_interface(InterfaceId)->
	case nnm_poller_loader:unschedule_interface(InterfaceId) of
		{ok,_}->
			nnm_dbcs_interface:remove_interface(InterfaceId);
		Else->
			Else
	end.

%% @spec find(Fields,Where,From,Count,Order)-> ({error,Reason}|Result)
%% where
%%		Fields = list()
%%		Where = [string()]
%%		From = integer()
%%		Count = integer
%%		Order = string()
%%		Reason = atom()
%%		Result = [[{key,value}]]
%% @doc find interface by complex conditions,'Where' is list of single condition ,each single condition is like "ip=192.168.0.1",
%% so Where is like ["ip=192.168.0.1","os=windows"],'From' and 'Count' is integer,Order is a orderby string like "ip@A",it's 
%% order by 'ip'(ascending) and "ip@D" is order by 'ip'(descending).
find(Fields,Where,From,Count,Order)->
	nnm_dbcs_interface:find(Fields,Where,From,Count,Order).
	
