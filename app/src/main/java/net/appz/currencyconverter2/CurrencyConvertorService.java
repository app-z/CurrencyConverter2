package net.appz.currencyconverter2;

import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;


public class CurrencyConvertorService {
    private static final String WEB_SERVICE_BASE_URL = "http://www.freecurrencyconverterapi.com/";
    private final WebService mWebService;

    public CurrencyConvertorService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WEB_SERVICE_BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mWebService = restAdapter.create(WebService.class);
    }


    public interface WebService {
		  @GET("/api/v2/countries")
		  Observable<ListCountryResponse> fetchListCountries();
	
		  @GET("/api/v2/convert")
          Observable<Map<String, ConvertCompactResponse>> fetchCompactResult( @Query("q") String q, @Query("compact") String compact);
	}

    public Observable<ListCountryResponse> fetchCountries() {
        return mWebService.fetchListCountries();
    }

    public Observable<Map<String, ConvertCompactResponse>> fetchConvert(String q, String compact) {
        return mWebService.fetchCompactResult(q, compact);
    }
}

