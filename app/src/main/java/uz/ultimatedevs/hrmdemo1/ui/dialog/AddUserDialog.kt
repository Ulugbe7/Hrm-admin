package uz.ultimatedevs.hrmdemo1.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import uz.ultimatedevs.hrmdemo1.R
import uz.ultimatedevs.hrmdemo1.data.User

class AddUserDialog(context: Context, private val user: User? = null) : AlertDialog(context) {
    private val btnCancel: Button
    private val btnConfirm: Button
    private val inputName: TextView
    private val inputLogin: TextView
    private val inputPassword: TextView
    private val inputProfession: TextView

    private var addDataListener: ((User) -> Unit?)? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_user, null, false)
        btnCancel = view.findViewById(R.id.btnCancel)
        btnConfirm = view.findViewById(R.id.btnConfirm)
        inputName = view.findViewById(R.id.inputName)
        inputLogin = view.findViewById(R.id.inputLogin)
        inputPassword = view.findViewById(R.id.inputPassword)
        inputProfession = view.findViewById(R.id.inputProfession)

        user?.let {
            inputName.text = user.name
            inputLogin.text = user.login
            inputPassword.text = user.password
            inputProfession.text = user.profession
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
        btnConfirm.setOnClickListener {
            confirmData(it)
        }
        setView(view)
    }

    fun setOnSaveClickListener(block: (User) -> Unit) {
        addDataListener = block
    }

    private fun confirmData(view: View) {
        if (addDataListener != null) {
            val name = inputName.text.toString()
            val login = inputLogin.text.toString()
            val password = inputPassword.text.toString()
            val profession = inputProfession.text.toString()
            when {
                name.isEmpty() -> {
                    inputName.error = "Ism kiriting"
                }
                login.isEmpty() -> {
                    inputLogin.error = "Login kiriting"
                }
                password.isEmpty() -> {
                    inputPassword.error = "Parol kiriting"
                }
                profession.isEmpty() -> {
                    inputProfession.error = "Kasbni kiriting"
                }

                else -> {
                    addDataListener?.invoke(
                        User(
                            user?.id ?: "",
                            name,
                            login,
                            password,
                            profession
                        )
                    )
                    dismiss()
                }
            }
        }
    }
}
