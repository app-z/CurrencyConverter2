package net.appz.currencyconverter2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends Activity implements OnItemSelectedListener, OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

	private Spinner spinnerFrom = null;
	private Spinner spinnerTo = null;
	private TextView textViewFrom;
	private TextView textViewTo;
	private Button buttonConvert;
	private TextView textViewCourse;
	private EditText editTextVal;
	private ProgressBar progressBar;

    private final List<Country> countriesList = new ArrayList<Country>();
//    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    final CurrencyConvertorService currencyConvertorService = new CurrencyConvertorService();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!AppHelper.isNetworkAvailable(this)) {
			AppHelper.serviceErrorMessage(this, "Internet Error", true);
		} else {

			// Spinners from -> to
			spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
			spinnerTo = (Spinner) findViewById(R.id.spinnerTo);
			textViewFrom = (TextView) findViewById(R.id.textViewFrom);
			textViewTo = (TextView) findViewById(R.id.textViewTo);
			textViewCourse = (TextView) findViewById(R.id.textViewCourse);
			editTextVal = (EditText)  findViewById(R.id.editTextVal);

			buttonConvert = (Button) findViewById(R.id.buttonConvert);
			buttonConvert.setOnClickListener(this);
			progressBar = (ProgressBar)findViewById(R.id.progressBar);

		}



        final Subscription subscription = currencyConvertorService.fetchCountries()
            .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListCountryResponse>() {
                    @Override
                    public void onNext(ListCountryResponse listCountryResponse) {
                        for(Map.Entry<String, Country> entry : listCountryResponse.countries.entrySet()) {
                            String key = entry.getKey();
                            Country value = entry.getValue();
                            countriesList.add(value);
                            Log.i(TAG, key + " : " + value.currencyId + " : "
                            		+ value.name + " : " + value.currencyName + " : "
                            		+ value.alpha3);
                        }
                        setSpinners(countriesList);
                    }

                    @Override
                    public void onCompleted() {
                        Crouton.makeText(MainActivity.this,
                                "Chose country", Style.INFO).show();
                    }

                    @Override
                    public void onError(final Throwable error) {
                        if (error instanceof RetrofitError
                                || error instanceof HttpException) {
                            Crouton.makeText(MainActivity.this,
                                    "Retrofit Error", Style.ALERT).show();
                            Log.e(TAG, error.getMessage());
                        } else {
                            Log.e(TAG, error.getMessage());
                            error.printStackTrace();
                            throw new RuntimeException("See inner exception");
                        }
                    }
                });
    }


	/*
	 * 
	 * 
	 * 
	 */
	void setSpinners(List<Country> countiesList){
		Collections.sort(countiesList);
		//Log.v(TAG, "After:" + countiesList.toString());
		int i = 0;
		String countriesFrom[] = new String[countiesList.size()];
		for (Country item : countiesList) {
			countriesFrom[i++] = item.currencyId + "-" + item.name;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, countriesFrom);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFrom.setAdapter(adapter);
		spinnerFrom.setOnItemSelectedListener(this);
		spinnerFrom.setSelection(AppHelper.getCountryFromIdx(this));

		spinnerTo.setAdapter(adapter);
		spinnerTo.setOnItemSelectedListener(this);
		spinnerTo.setSelection(AppHelper.getCountryToIdx(this));
		
		progressBar.setVisibility(View.GONE);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonConvert:
			String from_to = countriesList.get(AppHelper.getCountryFromIdx(this)).currencyId
			+ "_" + countriesList.get(AppHelper.getCountryToIdx(this)).currencyId;
			progressBar.setVisibility(View.VISIBLE);

            final Subscription subscription = currencyConvertorService.fetchConvert(from_to, "y")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Map<String, ConvertCompactResponse>>() {

                        @Override
                        public void onNext(Map<String, ConvertCompactResponse>  result) {
                            // Get the first entry that the iterator returns
                            Map.Entry<String, ConvertCompactResponse> entry = result.entrySet().iterator().next();
                            float course = entry.getValue().val;
                            float count = Float.valueOf(editTextVal.getText().toString());
                            textViewCourse.setText(String.valueOf(course * count));
                        }

                        @Override
                        public void onCompleted() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(final Throwable error) {
                            if (error instanceof RetrofitError
                                    || error instanceof HttpException) {
                                Crouton.makeText(MainActivity.this,
                                        "Retrofit Error", Style.ALERT).show();
                                Log.e(TAG, error.getMessage());
                            } else {
                                Log.e(TAG, error.getMessage());
                                error.printStackTrace();
                                throw new RuntimeException("See inner exception");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            break;
		}
	}


	@Override
	public void onItemSelected(AdapterView<?> parent,
			View view, int position, long id) {
		if(countriesList != null){
			switch (parent.getId()) {
			case R.id.spinnerFrom:
				textViewFrom.setText(countriesList.get(position).currencyName);
				AppHelper.setCountryFromIdx(this, position);
				break;
			case R.id.spinnerTo:
				textViewTo.setText(countriesList.get(position).currencyName);
				AppHelper.setCountryToIdx(this, position);
				break;
			default:
				break;
			}
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
