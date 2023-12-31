package com.gnusl.wow.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gnusl.wow.Models.User;

import org.json.JSONObject;

public class APIUtils {

    public static boolean isValidEmail(CharSequence target) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getAuthorization() {

        Log.d("TOKEN ",SharedPreferencesUtils.getToken());

        if (!SharedPreferencesUtils.getToken().isEmpty())
            return "Bearer "+SharedPreferencesUtils.getToken();
        else
            return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjhkZDRkMDg1OTQ3MmVlY2FiYTdhYjIyNWMwNjM2NTdkZWQxYTZkMGNmM2Y5MTU2OWQ0NDU4NWQ4NzFkMjQyZTU4OTgzNzI1MjMzOTE5YjdkIn0.eyJhdWQiOiIxIiwianRpIjoiOGRkNGQwODU5NDcyZWVjYWJhN2FiMjI1YzA2MzY1N2RlZDFhNmQwY2YzZjkxNTY5ZDQ0NTg1ZDg3MWQyNDJlNTg5ODM3MjUyMzM5MTliN2QiLCJpYXQiOjE1NDIwNjI1NzksIm5iZiI6MTU0MjA2MjU3OSwiZXhwIjoxNTczNTk4NTc5LCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.zLS5sxu8OSJ9WxutaFaT4rY0-quKYgB0rsBxrn-vQIbWZlqnRGi1BL47IMpqmCKaU0nODis9UZVVspkJLYTFiN__dYdJOhFRRnBVcagM4Ir69SKBRIMbco_a8vSthfDMyTW6lN8R6zXKW7tvWwHQbJt5pQ1B2pssL-41qxUZs_mmCU-BuXEOq8KGUjyXIQdriQXdSyRNSJDOhlY26RGBz93BCfhhjVPTbvxJbLLdraI8LaYDASiNJApCQ2jsm1hJ22knfVOzsm5evj8I9jXxf67yXPa_KWh4ZG9asOVUOwZsKqqBIgL9MR4UHs4YUMWoYXM6aF5jzzMHxyOWyYPiiUZlAiSUKrkyLTh2wEpR_LFc6kPyb4ArNvV6x32RSjz2yoKnrkh97iQE6Zi0qgHGjfw35S8D4fHiU2Mm5C2hwkn1fZL2YtEsnYO7bmNdApFrRWRCjJqX9uQuTRpAngOlK9hs0t_N9qCtkzC7-ReSyge_W4Uzyy-AkCJzbOLFpG40r9QvSPpgOqHrOEagzbAk6aV2aZYgsh-CKVpINqkTWdeqdDuSLeGYM3e8QPp0BtzPZkkns6mxfX1PedSOjlAR0qw96fyIDULXSgxDT8PhUcY6osdHc-vp9aMbX-fsFt_y7ctHhml3szC6NbLFdCjx2ImSDggJtkUQgYQhsrSFGjA";
    }

}
