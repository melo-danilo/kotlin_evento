package com.example.kotlin_evento

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_evento.utils.Connection
import com.example.kotlin_evento.utils.Convert
import com.example.kotlin_evento.viewModel.CheckingViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.alert_error.view.*
import kotlinx.android.synthetic.main.alert_ok.view.*

class CheckinActivity : AppCompatActivity() {

    private lateinit var viewAlertDialog: View
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        val id : Int = intent.extras?.get("id_event") as Int
        val title : String = intent.extras?.getString("title_event") as String
        val url : String = intent.extras?.getString("url_image") as String

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.ic_picasso_placeholder)
            .error(R.drawable.ic_picasso_not_suported)
            .into(image_evento)

        title_evento.text = title

        val viewModel = ViewModelProvider(this).get(CheckingViewModel::class.java)

        viewModel.getCheckingObserver().observe(this) {
            if (it != null) {
                showAlert(true)
            }else{
                showAlert(false)
            }
        }

        button_checkin.setOnClickListener {
            val nome: String = inputNome.text.toString()
            val email: String = inputEmail.text.toString()

            if (nome.isEmpty()){
                txt_nome.error = "Nome obrigatório"
            }else{
                txt_nome.error = null
            }

            if (email.isEmpty()){
                txt_email.error = "Email obrigatório"
            }else{
                if (!Convert.isValidEmail(email)) {
                    txt_email.error = "Email inválido"
                }else{
                    txt_email.error = null
                }
            }

            if(Connection.isOnline(this)) {
                if(nome.isNotEmpty() && email.isNotEmpty() && !Convert.isValidEmail(email)){
                    viewModel.makeApiCall(id.toString(), nome, email)
                }
            }else{
                Toast.makeText(this, "No Connected", Toast.LENGTH_SHORT).show()
            }
        }

        button_cancel.setOnClickListener{
            onBackPressed()
        }

        }

    private fun showAlert(isOk : Boolean) {
        if(isOk){
            viewAlertDialog = View.inflate(this@CheckinActivity, R.layout.alert_ok, null)

            viewAlertDialog.button_ok.setOnClickListener {
                dialog.dismiss()
                onBackPressed()
            }

        }else{
            viewAlertDialog = View.inflate(this@CheckinActivity, R.layout.alert_error, null)

            viewAlertDialog.button_error.setOnClickListener {
                dialog.dismiss()
                onBackPressed()
            }

        }
        val builder = AlertDialog.Builder(this@CheckinActivity)
        builder.setView(viewAlertDialog)

        dialog = builder.create()

        dialog.show()
    }
}