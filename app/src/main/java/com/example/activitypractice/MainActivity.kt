package com.example.activitypractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.activitypractice.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var job: Job? = null

    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"
    private var counter = 1
    private var randomValue = (1..100).random()
    private var btnbool: Boolean = false

//    1. 화면 회전시 랜덤변수, counter 유지하기
//    2. 카운터가 100이 되었을 때 화면을 회전하면 1로 바뀌는 원인 찾은 후, 회전 하더라도 100으로 유지되도록 출력하기
//    3. 홈 화면으로 갔을 때 멈추고 돌아오면 이어서 실행하기
//    4. 버튼을 클릭하여 멈춘 경우, 어떤 상황에서도 재실행 되어서는 안되도록 하기


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        savedInstanceState?.let {
//              counter = it.getInt("counter")
//          }
//        if (savedInstanceState != null) {
//            counter = savedInstanceState.getInt("counter")
//        }

        if (savedInstanceState != null) {
            randomValue = savedInstanceState.getInt("randomValue")
            btnbool = savedInstanceState.getBoolean("btnValue")
        }

//            counter = savedInstanceState.getInt("counter") // 이게 resotre로 가야함

//            binding.spartaTextView.text = counter.toString() // 이건 이미 setjob에 있었다.


//        if(savedInstanceState != null){
//            randomValue = savedInstanceState.getInt("randomValue")
//            counter = savedInstanceState.getInt("counter")
//
//        }//Bundle? 널이 들어갈 수 있기 때문에 if로 null 처리

        setupButton()
        setRandomValueBetweenOneToHundred()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        setJobAndLaunch()

    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        job?.cancel()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
        counter = savedInstanceState.getInt("counter")
        if (counter > 100) {
            counter = 100
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //        Log.i(TAG, "onSaveInstanceState")
        outState.putInt("counter", counter)
        outState.putInt("randomValue", randomValue)
        outState.putBoolean("btnValue", btnbool)
    }

    private fun setupButton() {
        binding.clickButton.setOnClickListener {
            checkAnswerAndShowToast()
            job?.cancel()
            btnbool = true
            Log.i(TAG, "값이 멈춤")
        }
    }

    private fun setRandomValueBetweenOneToHundred() {
        binding.textViewRandom.text = randomValue.toString()
    }


    private fun setJobAndLaunch() {
        job?.cancel() // job is uninitialized exception
        job = lifecycleScope.launch {
            if (isActive) {
                while (counter <= 100) {
                    Log.i(TAG, "count")
                    binding.spartaTextView.text = counter.toString()
                    if (btnbool) {
                        break
                    }
                    delay(100) // 1초 = 1000 // restore보다
                    counter += 1
                }
            }
        }
    }

    private fun checkAnswerAndShowToast() {
        val spartaText = binding.spartaTextView.text.toString()
        val randomText = binding.textViewRandom.text.toString()
        if (spartaText == randomText) {
            Toast.makeText(this, "훌륭합니다!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "아쉽습니다ㅠ", Toast.LENGTH_SHORT).show()
        }
    }
}

// - 화면 회전시 랜덤변수 유지하기
//
//    (회전을 하게 되면 랜덤변수 67이 33으로 바뀌게 됨)
//
//- 카운터가 100이 되었을 때 화면을 회전하면 1로 바뀌는 원인 찾은 후, 회전 하더라도 100으로 유지되도록 출력하기
//- 홈 화면으로 갔을 때 멈추고 돌아오면 이어서 실행하기
//- 버튼을 클릭하여 멈춘 경우, 어떤 상황에서도 재실행 되어서는 안되도록 하기

// 1. 처음 run : oncreate, start, resume
// 2. 화면 전환시 :
//2024-04-12 20:56:30.618 16096-16096 MainActivity            com.example.activitypractice         I  onPause
//2024-04-12 20:56:30.623 16096-16096 MainActivity            com.example.activitypractice         I  onStop
//2024-04-12 20:56:30.626 16096-16096 MainActivity            com.example.activitypractice         I  onSaveInstanceState
//2024-04-12 20:56:30.630 16096-16096 MainActivity            com.example.activitypractice         I  onDestroy
//2024-04-12 20:56:30.709 16096-16096 MainActivity            com.example.activitypractice         I  onCreate
//2024-04-12 20:56:30.795 16096-16096 MainActivity            com.example.activitypractice         I  onStart
//2024-04-12 20:56:30.796 16096-16096 MainActivity            com.example.activitypractice         I  onRestoreInstanceState
//2024-04-12 20:56:30.796 16096-16096 MainActivity            com.example.activitypractice         I  onResume

// 3. 홈화면으로 감녀 pause, stop, saveInstance
// 4. 다시 돌아오면 restart, start, onresume