package group4.tcss450.uw.edu.tcss450project.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class SendGetAsyncTask extends AsyncTask<Void, Void, String> {

    private final String mApiKey = "zyU7ox3IhJUs5FxA06MFl8uov3k1t9L3";

    // Required
    private final String mUrl;

    // Optional
    private final String mParamKey;
    private final String mParamValue;

    private Runnable mOnPre;
    private Consumer<String> mOnPost;
    private Consumer<String> mOnCancel;

    /**
     * Helper class for building GetAsyncTasks. Modelled on
     * the Builder class for SendPostAsyncTask.
     *
     * @author Charles Bryan, Jenna Hand
     */
    public static class Builder {

        //Required Parameters
        private final String mUrl;

        private String mParamKey = null;
        private String mParamValue = null;

        //Optional Parameters
        private Runnable onPre = () -> {};
        private Consumer<String> onPost = x -> {};
        private Consumer<String> onCancel = x -> {};

        /**
         * Constructs a new Builder.
         *
         * @param url the fully-formed url of the web service this task will connect to
         */
        public Builder(final String url) {
            mUrl = url;
        }

        public void setmParamKey(String paramKey) {
            mParamKey = paramKey;
        }

        public void setmParamValue(String paramValue) {
            mParamValue = paramValue;
        }

        /**
         * Set the action to perform during AsyncTask onPreExecute.
         *
         * @param val a action to perform during AsyncTask onPreExecute
         * @return
         */
        public SendGetAsyncTask.Builder onPreExecute(final Runnable val) {
            onPre = val;
            return this;
        }

        /**
         * Set the action to perform during AsyncTask onPostExecute.
         *
         * @param val a action to perform during AsyncTask onPostExecute
         * @return
         */
        public SendGetAsyncTask.Builder onPostExecute(final Consumer<String> val) {
            onPost = val;
            return this;
        }

        /**
         * Set the action to perform during AsyncTask onCancelled. The AsyncTask method cancel() is
         * called in doInBackGround during exception handling. Use this action to respond to
         * exceptional situations resulting from doInBackground execution. Note that external
         * cancellation will cause this action to execute.
         *
         * @param val a action to perform during AsyncTask onCancelled
         * @return
         */
        public SendGetAsyncTask.Builder onCancelled(final Consumer<String> val) {
            onCancel = val;
            return this;
        }

        /**
         * Constructs a SendPostAsyncTask with the current attributes.
         *
         * @return a SendPostAsyncTask with the current attributes
         */
        public SendGetAsyncTask build() {
            return new SendGetAsyncTask(this);
        }

    }

    /**
     * Construct a SendPostAsyncTask internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    public SendGetAsyncTask(final SendGetAsyncTask.Builder builder) {
        mUrl = builder.mUrl;
        mParamKey = builder.mParamKey;
        mParamValue = builder.mParamValue;

        mOnPre = builder.onPre;
        mOnPost = builder.onPost;
        mOnCancel = builder.onCancel;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mOnPre.run();
    }

    @Override
    protected void onCancelled(String result) {
        super.onCancelled(result);
        mOnCancel.accept(result);
    }

    @Override
    protected String doInBackground(Void... voids) {
        /*
        if (strings.length != 2) {
            throw new IllegalArgumentException("Two String arguments required.");
        }
        */
        String response = "";
        HttpURLConnection urlConnection = null;
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme("https")
                .appendPath(mUrl)
                .appendQueryParameter("apikey", mApiKey);
        if (mParamKey != null && mParamValue != null) {
            uriBuilder.appendQueryParameter(mParamKey, mParamValue);
        }
        Uri uri = uriBuilder.build();
        try {
            URL urlObject = new URL(uri.toString());
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            InputStream content = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
        } catch (Exception e) {
            response = "Unable to connect, Reason: "
                    + e.getMessage();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response;
    }
    @Override
    protected void onPostExecute(String result) {
        mOnPost.accept(result);
    }


}
