package ir.easazade.fuelkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choose.marsAppApiBtn
import kotlinx.android.synthetic.main.activity_choose.reqResBtn
import kotlinx.android.synthetic.main.activity_choose.simpleSingleTestsBtn

class ChooseActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_choose)

    marsAppApiBtn.setOnClickListener {
      val intent = Intent(this, MarsApiActivity::class.java)
      startActivity(intent)
    }

    simpleSingleTestsBtn.setOnClickListener {
      val intent = Intent(this, SimpleSingleRequestsActivity::class.java)
      startActivity(intent)
    }
    reqResBtn.setOnClickListener {
      val intent = Intent(this, ReqResDotIn::class.java)
      startActivity(intent)
    }
  }
}
