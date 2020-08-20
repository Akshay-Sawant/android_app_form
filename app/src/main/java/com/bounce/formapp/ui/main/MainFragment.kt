package com.bounce.formapp.ui.main

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.navigation.Navigation
import com.bounce.formapp.R
import com.bounce.formapp.data.APIClient
import com.bounce.formapp.data.response.APIActionResponse
import com.bounce.formapp.util.error.ErrorUtils
import com.bounce.formapp.util.others.AlertDialogUtils
import com.bounce.formapp.util.others.ValidationUtils
import com.bounce.formapp.util.server.ServerInvalidResponseException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    private lateinit var mTextInputLayoutName: TextInputLayout
    private lateinit var mTextInputEditTextName: TextInputEditText

    private lateinit var mTextInputLayoutPhone: TextInputLayout
    private lateinit var mTextInputEditTextPhone: TextInputEditText

    private lateinit var mTextInputLayoutAddress: TextInputLayout
    private lateinit var mTextInputEditTextAddress: TextInputEditText

    private lateinit var mTextInputLayoutEmail: TextInputLayout
    private lateinit var mTextInputEditTextEmail: TextInputEditText

    private lateinit var mTextInputLayoutCV: TextInputLayout
    private lateinit var mTextInputEditTextCV: TextInputEditText

    private lateinit var mMaterialButtonUploadCV: MaterialButton
    private lateinit var mMaterialButtonSubmit: MaterialButton

    private lateinit var mContentLoadingProgressBarMain: ContentLoadingProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTextInputLayoutName = view.findViewById(R.id.textInputLayoutName)
        mTextInputEditTextName = view.findViewById(R.id.textInputEditTextName)

        mTextInputLayoutPhone = view.findViewById(R.id.textInputLayoutPhone)
        mTextInputEditTextPhone = view.findViewById(R.id.textInputEditTextPhone)

        mTextInputLayoutAddress = view.findViewById(R.id.textInputLayoutAddress)
        mTextInputEditTextAddress = view.findViewById(R.id.textInputEditTextAddress)

        mTextInputLayoutEmail = view.findViewById(R.id.textInputLayoutEmail)
        mTextInputEditTextEmail = view.findViewById(R.id.textInputEditTextEmail)

        mTextInputLayoutCV = view.findViewById(R.id.textInputLayoutCV)
        mTextInputEditTextCV = view.findViewById(R.id.textInputEditTextCV)

        mMaterialButtonUploadCV = view.findViewById(R.id.materialButtonUploadCV)
        mMaterialButtonUploadCV.setOnClickListener(this@MainFragment)

        mMaterialButtonSubmit = view.findViewById(R.id.materialButtonSubmit)
        mMaterialButtonSubmit.setOnClickListener(this@MainFragment)

        mContentLoadingProgressBarMain = view.findViewById(R.id.contentLoadingProgressBarMain)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.materialButtonUploadCV -> {
            }
            R.id.materialButtonSubmit -> {
            }
        }
    }

    private fun isMainValidated() {
        when {
            !ValidationUtils.getValidationUtils()
                .isInputEditTextFilledFunc(
                    mTextInputEditTextName,
                    mTextInputLayoutName,
                    getString(R.string.text_error_empty_field)
                ) -> return
            !ValidationUtils.getValidationUtils()
                .isInputEditTextMobileFunc(
                    mTextInputLayoutPhone,
                    mTextInputEditTextPhone,
                    getString(R.string.text_error_mobile)
                ) -> return
            !ValidationUtils.getValidationUtils()
                .isInputEditTextFilledFunc(
                    mTextInputEditTextAddress,
                    mTextInputLayoutAddress,
                    getString(R.string.text_error_empty_field)
                ) -> return
            !ValidationUtils.getValidationUtils()
                .isInputEditTextEmailFunc(
                    mTextInputEditTextEmail,
                    mTextInputLayoutEmail,
                    getString(R.string.text_error_email_id)
                ) -> return
            !ValidationUtils.getValidationUtils()
                .isInputEditTextFilledFunc(
                    mTextInputEditTextCV,
                    mTextInputLayoutCV,
                    getString(R.string.text_error_empty_field)
                ) -> return
            else -> {

            }
        }
    }

    private fun onClearMain() {
        mTextInputEditTextName.text?.clear()
        mTextInputEditTextPhone.text?.clear()
        mTextInputEditTextAddress.text?.clear()
        mTextInputEditTextEmail.text?.clear()
        mTextInputEditTextCV.text?.clear()
    }

    private fun onClickSubmit() {
        context?.let {
            if (APIClient.isNetworkConnected(it)) {

                //pass it like this
                val file = File("getPath(filePath)!!")
                val requestFile: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                // MultipartBody.Part is used to send also the actual file name
                val body =
                    MultipartBody.Part.createFormData("image", file.name, requestFile)

                APIClient.apiInterface
                    .formSubmit(
                        mTextInputEditTextName.text.toString().trim()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        mTextInputEditTextPhone.text.toString().trim()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        mTextInputEditTextAddress.text.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        mTextInputEditTextEmail.text.toString().trim()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        body
                    )
                    .enqueue(object : Callback<APIActionResponse> {
                        override fun onResponse(
                            call: Call<APIActionResponse>,
                            response: Response<APIActionResponse>
                        ) {
                            if (response.isSuccessful) {
                                val mApiActionResponse: APIActionResponse? = response.body()
                                mContentLoadingProgressBarMain.visibility = View.GONE

                                if (mApiActionResponse != null) {
                                    if (mApiActionResponse.isActionSuccess) {
                                        AlertDialogUtils.getInstance().showAlert(
                                            it,
                                            R.drawable.ic_check_circle_black,
                                            "Success",
                                            mApiActionResponse.mMessage,
                                            getString(android.R.string.ok),
                                            null,
                                            DialogInterface.OnDismissListener {
                                                it.dismiss()
                                                onClickSubmit()
                                                view?.let { it1 ->
                                                    ValidationUtils.getValidationUtils()
                                                        .hideKeyboardFunc(
                                                            it1
                                                        )
                                                }
                                            }
                                        )
                                    } else {
                                        AlertDialogUtils.getInstance().showAlert(
                                            it,
                                            R.drawable.ic_warning_black,
                                            "Failed",
                                            mApiActionResponse.mMessage,
                                            getString(android.R.string.ok),
                                            null,
                                            DialogInterface.OnDismissListener {
                                                it.dismiss()
                                                onClickSubmit()
                                                view?.let { it1 ->
                                                    ValidationUtils.getValidationUtils()
                                                        .hideKeyboardFunc(
                                                            it1
                                                        )
                                                }
                                            }
                                        )
                                    }
                                } else {
                                    ErrorUtils.logNetworkError(
                                        ServerInvalidResponseException.ERROR_200_BLANK_RESPONSE +
                                                "\nResponse: " + response.toString(),
                                        null
                                    )
                                    AlertDialogUtils.getInstance().displayInvalidResponseAlert(it)
                                }
                            }
                        }

                        override fun onFailure(call: Call<APIActionResponse>, t: Throwable) {
                            ErrorUtils.parseOnFailureException(
                                it,
                                call,
                                t
                            )
                            mContentLoadingProgressBarMain.visibility = View.GONE
                        }
                    })
            } else {
                mContentLoadingProgressBarMain.visibility = View.GONE
                AlertDialogUtils.getInstance().displayNoConnectionAlert(it)
            }
        }
    }
}