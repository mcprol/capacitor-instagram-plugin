import { Plugins } from '@capacitor/core';
const { InstagramPlugin } = Plugins;
export class Instagram {
    shareLocalMedia(options) {
        return InstagramPlugin.shareLocalMedia(options);
    }
}
//# sourceMappingURL=plugin.js.map