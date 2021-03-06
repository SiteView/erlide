erl -setcookie erlide +P 500000 -env ERL_MAX_PORTS 50000 -env ERL_MAX_ETS_TABLES 20000 -env GETTEXT_DIR modules\erlang-gettext\priv -pa modules\erlang-gettext\ebin -pa modules\erlang-gettext\include -pa modules\erlsoap\ebin -pa modules\erlsom-1.2.1\ebin -pa modules\iconv -pa web\mochiweb -pa web\ebin -pa core\ebin -pa plugin\ebin -pa modules\df_snmp\ebin -pa modules\snmp\ebin -pa modules\esdl-1.0.1\ebin -pa modules\ssh\ebin -pa modules\GsmOperateUtils -pa modules/nnm/ebin -pa .\nitrogen\ebin .\nitrogen\include -pa sec\ebin -boot start_sasl -sname master  -eval "reloader:start_link(),application:start(quickstart_mochiweb),application:start(svecc),application:start(crypto),application:start(ssh),gettext:recreate_db()."