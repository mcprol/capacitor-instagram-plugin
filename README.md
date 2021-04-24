# capacitor-instagram-plugin
A ionic/capacitor plugin to share photos/videos with the Instagram app, both in 'feed' and 'story' channels

Only for Android.


## Usage
To share any media file in instagram (feed or story) you need:

1. download media files to local disk and move to a album (we are using this fork https://github.com/mcprol/media of capacitor-community/media)
```js
  ...
  import { Media, MediaAlbum } from '@capacitor-community/media';
  ...
  const media = new Media();
  ...
  async savePicture(url: string) {
    const albums = await media.getAlbums();
    ...
    return media.savePhoto({
      path: url,
      album: isPlatform('ios') ? album?.identifier : album?.name
    });
  }
  ...
  async saveVideo(url: string) {
    const albums = await media.getAlbums();
    ...
    return media.saveVideo({
      path: url,
      album: isPlatform('ios') ? album?.identifier : album?.name
    });
  }
  ...
  response = await photoGallery.saveVideo(url);
  this.localFilePaths.push(response.filePath);
```

2. copy text message to clipboard, as Instagram does not allow to share the text message. (we are using @capacitor/core)
```js
  import { ClipboardWrite } from "@capacitor/core";

  <ion-card-header>
    {{ clipboardText }}
  </ion-card-header>
  ...
  writeClipboard = async(options: ClipboardWrite) => await Clipboard.write(options);
  ...        
  const textToCopy: ClipboardWrite = {
    string: this.clipboardText,
    label: this.clipboardText,
  };
  ...
  writeClipboard(textToCopy);
```

3. Share to instagram with this plugin:
```js
  app = 'instagram://share';
  target = 'feed';
  ...
  app = 'instagram://camera';
  target = 'story';
  ...
  if (isPlatform('ios')) {
    window.open(app);
  } else {
    if (this.localFilePaths.length>1 && target==='feed') {
      // multiple medias to share at feed does not work with Instagram plugin, so we use the url shortcut
      window.open(app);
    } else {
      instagram.shareLocalMedia({
        medias: this.localFilePaths, 
        mediaType: this.mediaType, 
        target: target
      })
      .then(() => console.log('instagram.shareLocalMedia: ' + 'Shared!'))
      .catch((error: any) => console.error('instagram.shareLocalMedia: ' + error));
    }        
  }
```


## Android setup
On your `MainActivity.java` file add:
```java
import com.getcapacitor.community.media.MediaPlugin;
import com.metricool.plugins.instagram.InstagramPlugin;
...
public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the Bridge
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      add(MediaPlugin.class);
      add(InstagramPlugin.class);
    }});
  }
}
```


## Useful links:
https://developers.facebook.com/docs/instagram/sharing-to-stories/

https://devdactic.com/build-capacitor-plugin/
https://ionicframework.com/docs/native/instagram

https://github.com/gregavola/cordova-plugin-instagram-stories
https://github.com/vstirbu/InstagramPlugin

https://stackoverflow.com/questions/50460270/posting-multiple-images-to-instagram-using-intents
https://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android


## License
MIT