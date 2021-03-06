package rs.elfak.got.geopuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import rs.elfak.got.geopuzzle.library.Cons;

/**
 * Created by aleksandar on 6/12/2016.
 */
public class ScoreList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] names;
    private final String[] emails;
    private final String[] puzzlesSolved;

    public ScoreList(Activity context, String[] names, String[] emails, String[] puzzlesSolved) {
        super(context, R.layout.list_score_item, names);
        this.context = context;
        this.names = names;
        this.emails = emails;
        this.puzzlesSolved = puzzlesSolved;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_score_item, null, true);

        TextView nameText = (TextView) rowView.findViewById(R.id.txt);
        TextView puzzlesSolvedText = (TextView) rowView.findViewById(R.id.puzzlesSolvedTxt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        nameText.setText(names[position]);
        puzzlesSolvedText.setText(puzzlesSolved[position]);

        Object[] params = new Object[2];
        params[0] = Cons.KEY_UPLOADS_URL + emails[position] + ".jpg";
        params[1] = imageView;
        new DownloadImageTask().execute(params);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent friendProfile = new Intent(view.getContext(), ProfileActivity.class);
                friendProfile.putExtra(Cons.KEY_FULLNAME, names[position]);
                friendProfile.putExtra(Cons.KEY_EMAIL, emails[position]);
                view.getContext().startActivity(friendProfile);
            }
        });

        return rowView;
    }

    private class DownloadImageTask extends AsyncTask {
        ImageView imageView;

        @Override
        protected Object doInBackground(Object[] params) {
            imageView = (ImageView) params[1];
            return (Bitmap) loadImageFromNetwork((String) params[0]);
        }

        @Override
        protected void onPostExecute(Object o) {
            final Bitmap image = (Bitmap)o;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(image != null)
                        imageView.setImageBitmap((Bitmap) image);
                }
            });
        }

        private Bitmap loadImageFromNetwork(String url){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
                return bitmap;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
