-module(nnm_discovery_util).
-compile(export_all).

-define(CRYPTKEY,<<16#05,16#28,16#53,16#67,16#89,16#aa,16#bc,16#ef>>).
-define(IVEC,<<16#10,16#25,16#74,16#78,16#90,16#bb,16#cd,16#cf>>).

%% ------------------------------------------------------------------------------------------
%% write log
writeFile(Data,FilePath,Mode) ->
	{ok,Path} = file:get_cwd(), 
	{ok,File} = file:open(Path ++ "/nnm_discovery/" ++ FilePath, [Mode]),
	lists:foreach(fun(X) -> io:format(File, "~p~n", [X]) end, Data),
	file:close(File).

%% ------------------------------------------------------------------------------------------
%% integer list to string with split character
integerListToString(InList,Base,CStr) ->
	case erlang:is_list(InList) of
		true ->			
			CList = lists:map(fun(X) -> 
							  case erlang:is_integer(X) of
								  true ->
									  erlang:integer_to_list(X,Base);
								  false ->
									  X
							  end
					  end,
					  InList),
			RList = string:join(CList,CStr),
			RList;
		false ->
			""
	end.

%% ------------------------------------------------------------------------------------------
%% string split to integer list
stringToIntegerList(String,CStr) ->
	CList = string:tokens(String, CStr),
	RList = lists:map(fun(X) -> 
							  case string:to_integer(X) of
								  {error,_} ->
									  -1;
								  {Value,_} ->
									  Value
							  end
					  end, 
					  CList),
	RList.
	
%% ------------------------------------------------------------------------------------------
%% intefer to string
integerToString(Value) ->
	case is_integer(Value) of
		true ->
			integer_to_list(Value);
		false ->
			Value
	end.

%% to string
toString(Value) ->
	if
		erlang:is_list(Value) ->
			Value;
		erlang:is_integer(Value) ->
			erlang:integer_to_list(Value);
		erlang:is_atom(Value) ->
			erlang:atom_to_list(Value);
		erlang:is_float(Value) ->
			floatToString(Value,2);
		true ->
			""
	end.

toInteger(Value,_) when erlang:is_integer(Value) ->
	Value;
toInteger(Value,_) when erlang:is_list(Value) ->
	list_to_integer(Value);
toInteger(_,Default)->
	Default.		
		

%% floatToString(Value,Accuracy) ->
%% 	V1 = Value * math:pow(10, Accuracy),
%% 	V2 = erlang:integer_to_list(trunc(V1)),
%% 	if
%% 		length(V2) > Accuracy ->
%% 			string:left(V2, length(V2)-Accuracy) ++ "." ++ string:right(V2, Accuracy);
%% 		length(V2) =< Accuracy ->
%% 			"0." ++ string:copies("0", Accuracy-length(V2)) ++ V2
%% 	end.

%% ------------------------------------------------------------------------------------------
%% float to string
floatToString(Value,Accuracy) ->
	case erlang:is_float(Value) orelse erlang:is_integer(Value) of
		true ->
			case Accuracy of
				0 -> 
					erlang:integer_to_list(trunc(Value));
				_ ->
					P = "~."++integer_to_list(Accuracy)++"f",
    				lists:flatten(io_lib:format(P,[(Value + 0.0)]))
			end;
		false ->
			Value
	end.

%% ------------------------------------------------------------------------------------------
%% merger tow list delete same item
mergerList(List1,[]) -> List1;
mergerList(List1,[H|List2]) ->
	case lists:member(H, List1) of
		true ->
			mergerList(List1,List2);
		false ->
			mergerList([H|List1],List2)
	end.

mergerProplists(List1,[]) -> List1;
mergerProplists(List1,[H|List2]) ->
	{K2,_} = H,
	case proplists:get_value(K2,List1) of
		undefined -> mergerProplists([H|List1],List2);
		_ -> mergerProplists(lists:keyreplace(K2, 1, List1, H),List2)
	end.

%% ------------------------------------------------------------------------------------------
%% get local time format
getLocalTime() ->
	{{Y,M,D},{H,MM,S}} = calendar:local_time(),
	F = fun(X) ->
				V = integer_to_list(X),
				if
					length(V) < 2 ->
						"0" ++ V;
					true ->
						V
				end
		end,
	
	integer_to_list(Y) ++ "-" ++ F(M) ++ "-" ++ F(D) ++ " " ++ 	F(H) ++ ":" ++ F(MM) ++ ":" ++ F(S).
	
%% ------------------------------------------------------------------------------------------
%% format mac 
toMac(Mac) ->
%% 	io:format("Mac:~p~n",[Mac]),
	if
		erlang:is_list(Mac) andalso length(Mac) =:= 6 ->
			CMac = lists:map(fun(X) -> 
									 V = erlang:integer_to_list(X,16),
									 if
										 length(V) < 2 ->
											 "0" ++ V;
										 true ->
											 V
									 end
							 end,
							 Mac),
			[V1,V2,V3,V4,V5,V6] = CMac,
			V1 ++ V2 ++ "." ++ V3 ++ V4 ++ "." ++ V5 ++ V6;
		true ->
			""
	end.

%% ------------------------------------------------------------------------------------------
%% crypt 
encrypt(Data) ->
	Bin = term_to_binary(Data),
	Bin2 =
		case size(Bin) rem 8 of
			0 ->
				Bin;
			Rem ->
				L = list_to_binary(lists:duplicate(8-Rem,0)),
				<<Bin/binary,L/binary>>
		end,
			
%% 	crypto:start(),
	Result = crypto:des_cbc_encrypt(?CRYPTKEY, ?IVEC, Bin2),
%% 	crypto:stop(),
	Result.

decrypt(Data) ->
%% 	crypto:start(),
	Result = crypto:des_cbc_decrypt(?CRYPTKEY, ?IVEC, Data),
%% 	crypto:stop(),
	binary_to_term(Result).

%% ------------------------------------------------------------------------------------------
%% string replace 
stringReplace([], _, _) -> [];
stringReplace(String, S1, S2) when is_list(String), is_list(S1), is_list(S2) ->
	Length = length(S1),
	case string:substr(String, 1, Length) of 
		S1 -> 
			S2 ++ stringReplace(string:substr(String, Length + 1), S1, S2);
		_ -> 
			[hd(String)|stringReplace(tl(String), S1, S2)]
	end.

%% ------------------------------------------------------------------------------------------
%% regexp match
matches(String,RegExp,Keys) ->
	case regexp:matches(String, RegExp) of
		{match,Result} ->
			matches(Result,String,Keys,1,{},[]);
		_ ->
			[]
	end.

matches([],_,_,_,_,Data) -> Data;
matches([H|T],String,Keys,Index,Temp,Data) ->
	{Start,Length} = H,
	V = string:substr(String, Start, Length),
	{K,S,L} = lists:nth(Index, Keys),
%% 	io:format("K:~p,V:~p~n",[K,V]),
	case regexp:match(V, K) of
		{match,_,_} ->
			R = string:strip(string:substr(V, S, length(V)-L), both, $ ),
			io:format("R:~p,Index:~p~n",[R,Index]),
			if
				Index =:= length(Keys) ->
					Te = erlang:append_element(Temp, R),
					matches(T,String,Keys,1,{},Data++[Te]);
				true ->
					matches(T,String,Keys,Index+1,erlang:append_element(Temp, R),Data)
			end;
		_ ->
			if
				Index =:= 1 ->
					matches(T,String,Keys,1,{},Data);
				true ->
					matches([H|T],String,Keys,1,{},Data)
			end
	end.

%% ------------------------------------------------------------------------------------------
%% get ip from ip segment 
getIpList(IpStr) ->
	case string:rstr(IpStr, ",") of 
		0 ->
			case string:rstr(IpStr, "-") of
				0 ->
					[IpStr];
				_ ->
					[Ip1, Ip2] = string:tokens(IpStr, "-"),
					[I1,I2,I3,I4] = nnm_discovery_util:stringToIntegerList(Ip1, "."),
					[P1,P2,P3,P4] = nnm_discovery_util:stringToIntegerList(Ip2, "."),
					<<Int1:4/big-integer-unit:8>> = <<I1,I2,I3,I4>>,
					<<Int2:4/big-integer-unit:8>> = <<P1,P2,P3,P4>>,
					getIpListFromRange(Int1, Int2)
			end;
		_ ->
			IpSeg = string:tokens(IpStr, ","),
			lists:append([getIpList(X) || X <- IpSeg])
	end.

getIpListFromRange(Int1, Int2) when Int1 =:= Int2 + 1 -> [];
getIpListFromRange(Int1, Int2) -> 
	<<V1,V2,V3,V4>> = <<Int1:4/big-integer-unit:8>>,
	Ip = nnm_discovery_util:integerListToString([V1,V2,V3,V4],10, "."),
	[Ip|getIpListFromRange(Int1+1,Int2)].

tokens("",_,_) -> "";
tokens(String,Separator,IsNull) ->
	Index = string:str(String,Separator),
	if
		Index =:= 0 ->
			[String];
		true ->
			{Str1,Str2} = lists:split(Index+length(Separator)-1, String),
			if
				Str1 =:= Separator ->
					if
						IsNull ->
							[""|tokens(Str2,Separator,IsNull)];
						true ->
							tokens(Str2,Separator,IsNull)
					end;
				true ->
					[string:sub_string(Str1,1,string:str(Str1,Separator)-1)|tokens(Str2,Separator,IsNull)]
			end
	end.

subnet(Ip,Mask) ->
	if 
		Ip =:= "" -> "";
		Mask =:= "" -> "";
		true ->
			[M1,M2,M3,M4] = stringToIntegerList(Mask, "."),
			<<Mask1:4/big-integer-unit:8>> = <<M1,M2,M3,M4>>,
			Mask2 = erlang:integer_to_list(Mask1, 2),
			Ip ++ "/" ++ integer_to_list(string:chr(Mask2, $0) - 1)
	end.
			
			
			


