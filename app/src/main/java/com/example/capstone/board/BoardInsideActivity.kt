package com.example.capstone.board

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.RegisterActivity
import com.example.capstone.applyjob.ResumeListAdapter
import com.example.capstone.databinding.ActivityBoardInsideBinding
import com.example.capstone.databinding.CustomDialogBinding
import com.example.capstone.fragments.ResumeFragment
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Locale

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding: ActivityBoardInsideBinding
    private lateinit var key: String
    private var tts: TextToSpeech? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var resumewrite = true

    private lateinit var boardid: String

    private var mediaPlayer: MediaPlayer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
        binding.boardSettingicon.setOnClickListener {
            showDialog()
        }

        auth = FirebaseAuth.getInstance()


        // TextToSpeech 엔진 초기화
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS 엔진이 초기화 성공한 경우
                tts?.language = Locale.KOREAN
                tts?.setSpeechRate(0.7f) // 2배 빠른 속도


            } else {
                // TTS 초기화에 실패한 경우 처리
            }
        }





        key = intent.getStringExtra("key").toString()
        database = FirebaseDatabase.getInstance().reference
        val currentUserId = auth.currentUser?.uid


        getBoardData(key)
        getImageData(key)
        // Check if the current user has a resume and show/hide the apply job button accordingly
        checkUserHasResume()

        // ResumeListAdapter를 사용하여 리스트 뷰에 이력서 정보를 보여줌


        binding.applyjobBtn.setOnClickListener {
            val currentUserId = auth.currentUser?.uid

            // "resume" 테이블에서 프로필 이미지 URL 가져오기
            val resumeRef = database.child("resume").child(currentUserId ?: "")
            resumeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val resumeData = dataSnapshot.getValue(ResumeFragment.Resume::class.java)
                        val profileImageURL = resumeData?.profileImageURL ?: ""
                        val dataModel = dataSnapshot.getValue(BoardModel::class.java)




                        // "applyusers" 테이블에 프로필 이미지 URL 저장하기
                        val applyusersData = applyusers(
                            name = resumeData?.name ?: "",
                            address = resumeData?.address ?: "",
                            detail = resumeData?.detail ?: "",
                            birth = resumeData?.birth ?: "",
                            introduce = resumeData?.introduce ?: "",
                            sex = resumeData?.sex ?: "",
                            type = resumeData?.type ?: "",
                            boardid = boardid ?: " ",
                            profilePhotoURL = profileImageURL,
                            userid = FBAuth.getUid(),
                            tel = resumeData?.tel ?: " "


                        )

                        val applyusersRef = database.child("applyusers").child(currentUserId ?: "")
                        applyusersRef.setValue(applyusersData)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@BoardInsideActivity,
                                    "이력서 접수가 완료되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("BoardInsideActivity", "boardid: $key")
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    this@BoardInsideActivity,
                                    "이력서 접수에 실패하였습니다",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        // "resume" 테이블에 사용자 정보가 없을 경우 처리 (예: 이력서 작성 안 함 등)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "getUserResumeData:onCancelled", databaseError.toException())
                }
            })
        }

//        binding.boardMp3Btn.setOnClickListener {
//            checkJobField()
//        }

        binding.boardMp3Btn.setOnClickListener {
            val pay = binding.payArea.text.toString()
            val job = binding.categoryArea.text.toString()
            val worktime = binding.timeArea.text.toString()
            val day = binding.dayArea.text.toString()
            val textToSpeak = "보고 계신 회사는 지금  $job 로 일해줄 사람을 구하고 있습니다, 또한 이 회사의 시급은 $pay  이고, 이 회사는 $day 로 일해야 하고, 하루에 $worktime 시간 일하는 일꾼을 구하고 있습니다. 옆에 지원 접수버튼을 눌러서 이 회사의 일꾼이 되어보세요 "
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }

        // (기존 코드와 동일)
    }


    private fun showDialog() {
        // Create a custom dialog using custom_dialog.xml
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val dialogBinding = CustomDialogBinding.bind(mDialogView)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()

        dialogBinding.editBtn.setOnClickListener {
            alertDialog.dismiss()
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        dialogBinding.removeBtn.setOnClickListener {
            alertDialog.dismiss()
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    val userdataModel = dataSnapshot.getValue(RegisterActivity.User::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textAread.text = dataModel!!.content
//                    binding.timeArea.text = dataModel!!.time
                    binding.payArea.text = dataModel!!.pay + " 원"
                    binding.timeArea.text = dataModel!!.worktime
                    binding.dayArea.text = dataModel!!.day
                    binding.categoryArea.text = dataModel!!.job

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    boardid = writerUid



                    val userQuery: Query =
                        database.child("users").orderByChild("uid").equalTo(myUid)
                    if (myUid.equals(writerUid)) {
                        binding.boardSettingicon.isVisible = true
                    } else {
                        // Handle the case where the current user is not the author of the post
                    }

                    // Try to check if the user has a resume and show/hide the apply job button
                    checkUserHasResume()

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "삭제완료")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancellde", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun checkUserHasResume() {
        val myUid = FBAuth.getUid()
        if (myUid != null) {
            val resumeReference =
                FirebaseDatabase.getInstance().reference.child("resume").child(myUid)
            resumeReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // If the user has a resume, show the apply job button
                        binding.applyjobBtn.isVisible = true
                        binding.boardMp3Btn.isVisible = true
                        // Set an onClickListener to apply job button

                    } else {
                        // If the user does not have a resume, hide the apply job button
                        binding.applyjobBtn.isVisible = false
                        binding.boardMp3Btn.isVisible = false
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "checkUserHasResume:onCancelled", databaseError.toException())
                }
            })
        } else {
            // Handle the case where the user is not logged in
            // You may want to hide the apply job button or redirect the user to login page, etc.
            binding.applyjobBtn.isVisible = false
        }
    }

    private fun getImageData(key: String) {
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("$key.jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                // Handle the failure to load the image
            }
        })
    }

    private fun checkJobField() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("board").child(key)

        val jobSoundMap = mapOf(
            "의류 매장 보조" to R.raw.cloth_introduce_mp3,
            "편의점" to R.raw.convenience_introduce_mp3,
            "사무보조" to R.raw.office_introduce_mp3,
            "빵집 보조" to R.raw.bakery_introduce_mp3,
            "도서관 사서" to R.raw.library_introduce_mp3
        )

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val job = dataSnapshot.child("job").getValue(String::class.java)
                    val soundResource = jobSoundMap[job]

                    soundResource?.let { resource ->
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer.create(this@BoardInsideActivity, resource)
                        mediaPlayer?.start()

                        mediaPlayer?.setOnCompletionListener {
                            mediaPlayer?.release()
                            mediaPlayer = null
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })
    }


    data class applyusers(
        val name: String = "",
        val address: String = "",
        val detail: String = "",
        val birth: String = "",
        val introduce: String = "",
        val sex: String = "",
        val type: String = "",
        val boardid: String = "",
        val profilePhotoURL: String = "", // Include profileImageURL in the data class properties
        val userid : String = " ",
        val tel : String = " "
    ) {
        // Add a setter for profileImageURL
        fun setProfileImageURL(profileImageURL: String) = copy(profilePhotoURL = profileImageURL)
    }
}