package com.metricool.plugins.instagram;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.getcapacitor.JSArray;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import java.io.File;
import java.util.ArrayList;

@NativePlugin(
    requestCodes = {
        InstagramPlugin.REQUEST_SHARE
    }
)
public class InstagramPlugin extends Plugin {

    private static final String LOGTAG = "InstagramPlugin";
    protected static final int REQUEST_SHARE = 2020;

    private static final String MEDIA_IMAGE = "image";
    private static final String MEDIA_VIDEO = "video";

    private static final String TARGET_FEED = "feed";
    private static final String TARGET_STORY = "story";

    @PluginMethod
    public void shareLocalMedia(PluginCall call) {
        JSArray medias = call.getArray("medias");
        if (medias == null || medias.length()==0) {
            call.error("Missing 'medias' argument");
            return;
        };

        String mediaType = call.getString("mediaType");
        if (mediaType == null || mediaType.length()==0) {
            call.error("Missing 'mediaType' argument");
            return;
        };

        String target = call.getString("target");
        if (target == null || target.length()==0) {
            call.error("Missing 'target' argument");
            return;
        };

        Log.d(LOGTAG, "shareLocalMedia: '" + medias + "', '" + mediaType + "', '" + target + "'");
        shareLocalMedia(call, medias, mediaType, target);
    }


    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);

        // Get the previously saved call
        PluginCall call = getSavedCall();
        if (call == null) {
            return;
        }

        if (requestCode == REQUEST_SHARE) {
            call.success();
        } else {
            call.error("Internal error. Unknown request code: " + requestCode);
        }
    }


    private void shareLocalMedia(PluginCall call, JSArray medias, String mediaType, String target) {
        saveCall(call);

        try {
            boolean multipleMedia = medias.length() > 1;

            // choose the right action
            String action = Intent.ACTION_SEND;
            if (multipleMedia || TARGET_STORY.equalsIgnoreCase(target)) {
                // multiple medias are only enable to share in 'stories', so 'target' parameter is discarded.
                action = Intent.ACTION_SEND_MULTIPLE;
                multipleMedia = true;
            } else {
                action = "com.instagram.share.ADD_TO_" + TARGET_FEED.toUpperCase();
            }

            Intent  shareIntent = new Intent(action);

            if (multipleMedia) {
                ArrayList<Uri> uris = new ArrayList<Uri>();

                for (int i=0; i<medias.length(); i++) {
                    File file = new File(medias.getString(i));
                    Uri uri = getUriForFile(file);
                    uris.add(uri);
                }
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                shareIntent.setType(mediaType + "/*");
            } else {
                File file = new File(medias.getString(0));
                Uri uri = getUriForFile(file);

                shareIntent.setType(mediaType + "/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            }

            shareIntent.setPackage("com.instagram.android");
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Log.v(LOGTAG, "Sharing '" + action + "', '"+ mediaType + "', '" + medias + "'");
            startActivityForResult(call, shareIntent, REQUEST_SHARE);
        } catch (Exception e) {
            call.error("Internal error: " + e.getMessage());
        }
    }

    private Uri getUriForFile(File file) {
        //Uri uri = Uri.fromFile(file);
        AppCompatActivity activity = this.getActivity();

        // Really not clean, as it depends on the package in our MainActivity program.
        String authority = "com.ionicframework.metricool185346.fileprovider";

        Uri uri = FileProvider.getUriForFile(activity, authority, file);
        return uri;
    }
}
