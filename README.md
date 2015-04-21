Currency Converter 2
=================

Currency Converter RX Java, Reprofit version

This Android Application get feed from http://www.freecurrencyconverterapi.com/

![](https://github.com/app-z/CurrencyConverter2/blob/master/images/device-2014-12-21-030019.png)

Request:

http://www.freecurrencyconverterapi.com/api/v3/convert?q=USD_PHP&compact=y

Response:
```
{
"USD_PHP": 
{"val": 44.2055}
}
```
Code:
```
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
```
=================

The MIT License

Copyright (c) 2014 Appz (http://app-z.net)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
