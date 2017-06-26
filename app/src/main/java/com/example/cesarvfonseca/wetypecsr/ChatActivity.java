package com.example.cesarvfonseca.wetypecsr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {

    private EditText etUsuario,etClave;
    private Button btnIngresar;

    private RequestQueue mRequest;
    private VolleyRP volley;

    String vUsr,vPw;

    private String USUARIO = "";
    private String CLAVE = "";
    private static String servidorIP="http://webdesigns.hol.es/appwetype/Login_GETID.php?usr=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        etUsuario=(EditText)findViewById(R.id.etUsuario);
        etClave=(EditText)findViewById(R.id.etClave);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ChatActivity.this,"Ingreso exitoso",Toast.LENGTH_SHORT).show();
                vUsr=etUsuario.getText().toString().toLowerCase();
                vPw=etClave.getText().toString().toLowerCase();
                validarDatos(vUsr,vPw);
            }
        });
    }

    public void validarDatos(String usr,String pw){
        USUARIO = usr;
        CLAVE = pw;
        //Toast.makeText(this,usr+" - "+pw,Toast.LENGTH_SHORT).show();
        verJSON(servidorIP+usr);
    }

    public void verJSON(String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                validarLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this,"Ocurrio un error, favor de contactar a soporte",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void validarLogin(JSONObject datos){
        //Toast.makeText(this,"Los datos son: "+datos.toString(),Toast.LENGTH_SHORT).show();
        try {
            String rs = datos.getString("resultado");
            if (rs.equals("CC")) {
                Toast.makeText(this, "Usuario existe", Toast.LENGTH_SHORT).show();
                JSONObject jsonDatos = new JSONObject(datos.getString("datos"));
                String jUser = jsonDatos.getString("usuario");
                String jClave = jsonDatos.getString("clave");
                if (jUser.equals(USUARIO) && jClave.equals(CLAVE)){
                    Toast.makeText(this,"Usuario y contraseña correctos", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this,, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, rs , Toast.LENGTH_SHORT).show();
            }
        }catch(JSONException e){

        }
    }

}
