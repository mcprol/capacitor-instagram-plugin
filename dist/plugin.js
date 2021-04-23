var capacitorPlugin = (function (exports, core) {
    'use strict';

    const { InstagramPlugin } = core.Plugins;
    class Instagram {
        shareLocalMedia(options) {
            return InstagramPlugin.shareLocalMedia(options);
        }
    }

    exports.Instagram = Instagram;
    Object.keys(core).forEach(function (k) {
        if (k !== 'default' && !exports.hasOwnProperty(k)) Object.defineProperty(exports, k, {
            enumerable: true,
            get: function () {
                return core[k];
            }
        });
    });

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
