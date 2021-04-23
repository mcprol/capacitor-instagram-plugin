import { InstagramPluginProtocol, InstagramShareOptions } from './definitions';
export declare class Instagram implements InstagramPluginProtocol {
    shareLocalMedia(options: InstagramShareOptions): Promise<{
        results: any[];
    }>;
}
