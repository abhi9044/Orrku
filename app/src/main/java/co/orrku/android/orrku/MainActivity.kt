package co.orrku.android.orrku

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.DefaultCallback
import android.annotation.SuppressLint
import java.io.File
import android.graphics.BitmapFactory
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.DatePicker
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private final var MY_PERMISSIONS_REQUEST_CAMERA = 0
    @RequiresApi(Build.VERSION_CODES.N)
    var calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_add_image.setOnClickListener(this)
        edt_time_from.setOnClickListener(this)
        edt_time_to.setOnClickListener(this)
        edt_valid_till.setOnClickListener(this)
        requestCameraPermission()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v!!.getId()) {
            R.id.btn_add_image -> EasyImage.openChooserWithGallery( this, "Select",  0)
            R.id.edt_time_from -> showTimePicker(R.id.edt_time_from)
            R.id.edt_time_to -> showTimePicker(R.id.edt_time_to)
            R.id.edt_valid_till -> showDatePicker()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                val myBitmap = BitmapFactory.decodeFile(imageFile!!.getAbsolutePath())
                img_offer.setImageBitmap(myBitmap)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
            }

            fun onImagesPicked(imagesFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                //Handle the image
                val myBitmap = BitmapFactory.decodeFile(imagesFiles.last().getAbsolutePath())
                img_offer.setImageBitmap(myBitmap)
            }
        })

    }

    fun requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        MY_PERMISSIONS_REQUEST_CAMERA)
            }

        }
    }
    @SuppressLint("NewApi")
    fun showTimePicker(id: Int) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            if (id == R.id.edt_time_from){
                edt_time_from.setText(selectedHour.toString() + ":" + selectedMinute)
            }else{
                edt_time_to.setText(selectedHour.toString() + ":" + selectedMinute)
            }
        }, hour, minute, true)//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showDatePicker(){
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            edt_valid_till.setText(sdf.format(calendar.time))
        }, year, month, day)
        dpd.show()
    }

}
