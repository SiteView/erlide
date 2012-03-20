%% 
%% @doc api of nnm nodes
%% @version{1.0}
%% @copyright 2010 dragonflow.com
%% @author Shi xianfang <xianfang.shi@dragonflow.com>

-module(nnm_api_node).
-compile(export_all).

-export([get_list/0,get_list/1,get_node/2,create_node/1,update_node/1,delete_node/1,get_interfaces/1,get_resource_list/1,find/5]).

%% @spec get_list()-> ({error,Reason}|Result)
%% where
%%		Reason = atom()
%%		Result = [[{key,value}]]
%% @doc get list of node
%%
get_list()->
	nnm_dbcs_node:get_all().
	
	
%% @spec get_list(Fields)-> ({error,Reason}|Result)
%% where
%%		Fields = list()
%%		Reason = atom()
%%		Result = [[{key,value}]]
%% @doc get list of node
%%
get_list(Fields)->
	nnm_dbcs_node:get_list(Fields).
	
	
%% @spec get_node(Id,Fields)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Fields = list()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get detail information of node
%%
get_node(Id,Fields)->
	nnm_dbcs_node:get_node(Id,Fields).

%% @spec get_detail(Id)-> ({error,Reason}|Result)
%% where
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get detail information of node
%%
get_detail(Id)->
	nnm_dbcs_node:get_node(Id).
	
%% @spec get_perform(Id)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc get performate information of node
%%
get_perform(Id)->
	nnm_dbcs_node:get_perform(Id).

%% @spec get_node_value(Id,Key)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Key = atom()
%%		Reason = atom()
%%		Result = term()
%% @doc get a single value of node
%%	
get_node_value(Id,Key)->
	nnm_dbcs_node:get_value(Id,Key).
	
%% @spec create_node(Node)-> ({error,Reason}|{ok,Result})
%% where
%%		Node = [{key,value}]
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc create a new node
%%
create_node(Node)->
	case nnm_dbcs_node:create_node(Node) of
		{ok,Data}->
			Id = proplists:get_value(id,Data),
			case nnm_poller_loader:schedule_node(Id) of
				{ok,_}->
					{ok,Data};
				Else2->
					Else2
			end;
		Else->
			Else
	end.
	
%% @spec update_node(Node)-> ({error,Reason}|{ok,Result})
%% where
%%		Interface = [{key,value}]
%%		Reason = atom()
%%		Result = [{key,value}]
%% @doc create a new interface
%%	
update_node(Node)->
	case nnm_dbcs_node:update_node(Node) of
		{ok,Data}->
			Id = proplists:get_value(id,Data),
			case nnm_poller_loader:unschedule_node(Id) of
				{ok,_}->
					nnm_poller_loader:schedule_node(Id),
					{ok,Data};
				Else->
					Else
			end;
		Else->
			Else
	end.
	
%% @spec delete_node(Id)-> ({error,Reason}|{ok,Result})
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = term()
%% @doc delete a node by id
%%
delete_node(NodeId)->
	case get_interfaces(NodeId) of
		{error,_}->
			pass;
		Interfaces->
			F = fun(X)->
				case proplists:get_value(id,X) of
					undefined->
						pass;
					Id->
						case nnm_api_interface:delete_interface(Id) of
							{ok,_}->
								ok;
							_->
								error
						end
				end
			end,
			Ret = [F(X)||X<-Interfaces],
			case lists:member(error,Ret) of
				true->
					{error,"delete interface error"};
				_->
					case nnm_poller_loader:unschedule_node(NodeId) of
						{ok,_}->
							nnm_dbcs_node:remove_node(NodeId);
						Else->
							Else
					end
			end
	end.

%% @spec get_interfaces(Id)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [Inteface]
%%		Inteface =[{key,value}]
%% @doc query a node's interfaces
%%
get_interfaces(NodeId)->
	nnm_dbcs_interface:get_by_node(NodeId).
	
	
%% @spec get_interfaces(Id,Fields)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Fields = [atom()]
%%		Reason = atom()
%%		Result = [Inteface]
%%		Inteface =[{key,value}]
%% @doc query a node's interfaces
%%
get_interfaces(NodeId,Fields)->
	nnm_dbcs_interface:get_by_node(NodeId,Fields).
	
%% @spec get_resource_list(Params)-> ({error,Reason}|Result)
%% where
%%		Id = atom()
%%		Reason = atom()
%%		Result = [Inteface]
%%		Inteface =[{key,value}]
%% @doc list a node's resource
%%	
get_resource_list(Params)->
	nnm_node:find_interface(Params).

	
%% @spec find(Fields,Where,From,Count,Order)-> ({error,Reason}|Result)
%% where
%%		Fields = list()
%%		Where = [string()]
%%		From = integer()
%%		Count = integer
%%		Order = string()
%%		Reason = atom()
%%		Result = [[{key,value}]]
%% @doc find node by complex conditions,'Where' is list of single condition ,each single condition is like "ip=192.168.0.1",
%% so Where is like ["ip=192.168.0.1","os=windows"],'From' and 'Count' is integer,Order is a orderby string like "ip@A",it's 
%% order by 'ip'(ascending) and "ip@D" is order by 'ip'(descending).
find(Fields,Where,From,Count,Order)->
	nnm_dbcs_node:find(Fields,Where,From,Count,Order).

	
