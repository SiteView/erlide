%% This is the application resource file (.app file) for the 'base'
%% application.
{application, nnm,
[{description, "siteview nnm" },
{vsn, "1.0" },
{modules, [nnm_app, nnm_sup,nnm_poller_loader]},
{registered,[nnm_app]},
{applications, [kernel,stdlib,svecc]},
{mod, {nnm_app,[]}},
{start_phases, []}
]}.