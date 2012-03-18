%% This is the application resource file (.app file) for the 'base'
%% application.
{application, asset,
[{description, "siteview asset manager" },
{vsn, "1.0" },
{modules, [asset_app]},
{registered,[asset_app]},
{applications, [kernel,stdlib]},
{mod, {asset_app,[]}},
{start_phases, []}
]}.