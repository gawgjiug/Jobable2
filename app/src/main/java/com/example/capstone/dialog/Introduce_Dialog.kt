package com.example.capstone.dialog

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.capstone.R
import com.example.capstone.databinding.IntroduceDialogBinding
import com.example.capstone.fragments.ResumeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


interface IntroduceDialogListener {
    fun onIntroduceTextSelected(text: String)
}


class Introduce_Dialog : DialogFragment() {



    private var _binding: IntroduceDialogBinding? = null
    private val binding get() = _binding!!
    private val selectedImages = mutableListOf<String>()

    private var listener: IntroduceDialogListener? = null
    private var tts: TextToSpeech? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var User_name: String = "" // 선언 및 초기화
    private var User_type: String = "" // 선언 및 초기화


    // 리스너 설정
    fun setIntroduceDialogListener(listener: IntroduceDialogListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroduceDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()



        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS 엔진이 초기화 성공한 경우
                tts?.language = Locale.KOREAN
                tts?.setSpeechRate(0.5f) // 2배 빠른 속도


            } else {
                // TTS 초기화에 실패한 경우 처리
            }
        }

        binding.introduceOrganizeSound.setOnClickListener {

            val textToSpeak = "평소에 어질러진 물건을 잘 정리하고 정돈하는 것에 자신 있으시다면 " +
                    "위 이미지를 클릭해서 사장님에게 회원님의 장점에 대해 소개할 수 있습니다"

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)

        }
        binding.introduceCollaborationSound.setOnClickListener {

            val textToSpeak = "평소에 친구들과 사이좋게 지내는 거나 주변 동료들과 협동하는 것에 자신 있으시다면" +
                    "위 이미지를 클릭해서 사장님에게 회원님의 장점에 대해 소개할 수 있습니다"

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }
        binding.introduceSinceritySound.setOnClickListener {

            val textToSpeak = "평소에 맡은 일을 성실하게 해내는 것에 자신 있으시다면 " +
                    "위 이미지를 클릭해서 사장님에게 회원님의 성실함에 대해 소개할 수 있습니다"

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }

        binding.introduceKeeptimeSound.setOnClickListener {
            val textToSpeak = "평소에 시간약속을 잘 지키고 지각을 하지 않는 장점을 가지고 계시면 " +
                    "위 이미지를 클릭해서 사장님에게 회원님의 장점에 대해 소개할 수 있습니다"

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }
        binding.introduceSmileSound.setOnClickListener {
            val textToSpeak = "평소에 모든 손님들에게 밝게 웃으며 응대하거나, 밝은 모습으로 인사할 수 있다면 " +
                    "위 이미지를 클릭해서 사장님에게 회원님의 장점에 대해 소개할 수 있습니다"

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }

        binding.introduceStrongSound.setOnClickListener {
            val textToSpeak = "평소에 무거운 것을 잘 들고 오래 서 있는 것에 자신 있다면 " +
                    "위 이미지클 클릭해서 사장님에게 회원님의 장점에 대해 소개할 수 있습니다 "

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }

        binding.introduceMp3Btn.setOnClickListener {
            val textToSpeak = "위 6개의 이미지 중에서 회원님이 사장님에게 소개하고 싶은 3가지 장점을 클릭하시고 " +
                    "작성하기 버튼을 누르시면 선택 된 3가지 장점과 관련된 자기소개서를 작성하실 수 있습니다  "

            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH,null,null)
        }








        // 이미지뷰와 컬러 이미지뷰를 맵으로 관리
        val imageViewToColorViewMap = mapOf(
            binding.introduceCollaboration to binding.introduceCollaborationColor,
            binding.introduceKeeptime to binding.introduceKeeptimeColor,
            binding.introduceOrganize to binding.introduceOrganizeColor,
            binding.introduceSmile to binding.introduceSmileColor,
            binding.introduceSincerity to binding.introduceSincerityColor,
            binding.introduceStrong to binding.introduceStrongColor
        )

        imageViewToColorViewMap.forEach { (imageView, colorView) ->
            imageView.setOnClickListener {
                if (imageView.isVisible) {
                    imageView.isVisible = false
                    colorView.isVisible = true
                    selectedImages.add(getImageTag(imageView))
                } else {
                    imageView.isVisible = true
                    colorView.isVisible = false
                    selectedImages.remove(getImageTag(imageView))
                }
            }

            colorView.setOnClickListener {
                colorView.isVisible = false
                imageView.isVisible = true
                selectedImages.remove(getImageTag(imageView))
            }
        }

        binding.introduceApplyBtn.setOnClickListener {

            val resumeRef = database.child("resume").child(auth.currentUser?.uid ?: "")
            resumeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val resume = snapshot.getValue(ResumeFragment.Resume::class.java)
                        resume?.let {

                            val User_name = it.name
                            val User_type = it.type


                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })



            if (selectedImages.size < 3 || selectedImages.size > 3) {
                Toast.makeText(requireContext(), "이미지를 3개 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_4"))) {

                val text = "안녕하세요 저의 이름은  $User_name 이고 일을 할 때 주변 정리정돈을 잘합니다, 뿐만 아니라 저는 친화력이 좋기 때문에 " +
                        "같이 일하는 동료와 항상 좋은 관계를 유지할 수 있으며 항상 약속된 시간을 지키는 것을 중시하기 때문에 " +
                        "출근 시간에 맞춰 늦지 않도록 노력하는 사람입니다"

                // 리스너가 설정되어 있는지 확인하고 텍스트 전송
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_3"))) {
                val text = "안녕하세요 저는 일을 할때 주변 정리를 잘 수행할 수 있습니다,  뿐만 아니라 저는 친화력이 좋기 때문에 같이 일하는 " +
                        "동료 또는 손님들과 항상 좋은 관계를 유지해왔으며, 또한  누구에게도 뒤쳐지지 않는 성실함을 가지고 있기 때문에 주어진 업무를" +
                        "끝까지 책임질 수 있는 사람입니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_5"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘 수행할 수 있습니다, 뿐만 아니라 저는 친화력이 좋기 때문에 같이 일하는 동료와 항상 좋은 " +
                        "관계를 유지 할 수 있었습니다, 또, 저는 항상 모두에게 밝은 미소로 응대하거나 인사하는 습관을 가지고 있기 때문에 " +
                        "손님들에게 항상 좋은 인상을 남길 수 있었습니다."
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_6"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘 수행하고 항상 주변을 깔끔하게 유지할 수 있습니다 , 뿐만 아니라 저는 친화력이 좋기 때문에" +
                        "같이 일하는 동료 또는 손님들과 항상 좋은 관계를 유지할 수 있었으며, 또 저는 무거운 물건을 들거나 오래 서있거나 강한 힘을 요구하는 작업도" +
                        "무리없이 수행할 수 있는 장점을 가지고 있습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_4"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘하고 , 항상 주변을 깔끔하게 정리할 수 있습니다, 뿐만 아니라 저는 항상 맡은 일을" +
                        "미루지 않고 성실하게 처리하는 편이며, 또한 저는 근무지에 지각하는일 없이 항상 시간약속을 철저하게 지킬 수 있는 사람입니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_5"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘하고, 항상 주변을 깔끔하게 정리할 수 있습니다 ,  뿐만 아니라 저는 항상 맡은 일을 " +
                        "미루지 않고 성실하게 수행하는 편이며 , 또한 저는 항상 손님이나 동료를 대할 때 밝게 웃으면서 대화하기 때문에 손님 또는 동료와 " +
                        "항상 친밀한 관계를 유지할 수 있습니다."
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_6"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘하고 항상 주변을 깔끔하게 정리정돈 할 수 있습니다 , 뿐만 아니라 저는 항상 맡은 일을 " +
                        "미루지 않고 주어진 시간내에 성실하게 수행하는 편이며 , 또한 저는 무거운 물건을 들거나 , 오래 서있는 작업을 할때 문제 되지 않는 " +
                        "체력을 가지고 있습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_4", "introduce_color_5"))) {
                val text = "안녕하세요 저는 일을 할 때 주변 정리를 잘하며, 항상 제 주변을 깔끔하게 정리정돈 할 수 있습니다 , 뿐만 아니라 저는 항상 지각하는 일 없이 " +
                        "근무시간을 철저하게 지키며 , 업무를 할 때 주어진 시간내에 업무를 완수하는 것을 중요시 합니다 . 또한 저는 손님 혹은 동료들에게 항상 밝게 웃으며 " +
                        "인사하는 습관을 가지고 있기 때문에 손님 , 동료 들과 항상 좋은 관계를 유지할 수 있습니다."
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_4", "introduce_color_6"))) {
                val text = "안녕하세요 저는 일을 할 때 항상 주변 정리를 잘하며 , 제 주변을 깔끔하게 정돈 된 상태로 유지할 수 있습니다 . 뿐만 아니라 저는 지각하는 일 없이" +
                        "항상 근무시간을 철저하게 지키며 업무를 할 때 주어진 시간내에 업무를 처리하는 것을 중요시 합니다 , 또한 저는 무거운 물건이나 신체적인 능력을 요구하는 업무도" +
                        "무리없이 소화할 수 있는 능력을 가지고 있습니다  "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_5", "introduce_color_6"))) {
                val text = "안녕하세요 저는 일을 할때 항상 주변 정리를 잘하며 , 제 주변을 깔끔하게 정돈 된 상태로 유지할 수 있습니다  뿐만 아니라 저는 손님을 응대할 때나" +
                        "동료와 소통할 때 항상 밝게 웃으면서 상대방을 존중하기 때문에 손님 혹은 동료들과 좋은 관계를 유지할 수 있었습니다 , 또한 저는 무거운 물건이나 신체적인 능력을 " +
                        "요구하는 업무도 무리없이 소화할 수 있는 능력을 가지고 있습니다 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_4"))) {
                val text = "안녕하세요 저는 일을 할 때 항상 맡은 업무를 성실하게 수행하며 기한 안에 처리하는 능력을 가지고 있습니다 , 뿐만 아니라 저는 늘 동료들 혹은 타인들과 좋은 관계를 유지하며 " +
                        "협동 능력이 필요한 업무를 능숙히 수행할 수 있습니다 , 또한 저는 시간약속을 중요시 하기 때문에 출근시간 또는 업무 기한을 철저히 지킬 수 있는 장점을 가지고 있습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_5"))) {
                val text = "안녕하세요 저는 일을 할때 항상 주변 동료 혹은 타인들과 좋은 관계를 유지하고, 협동하는 능력을 가지고 있습니다 , 뿐만 아니라 저는 맡은 업무를 뒤로 미루는 일 없이" +
                        "책임감있고 성실하게 수행하는 자세를 가지고 있습니다 , 또한 손님 응대 혹은 동료와 소통할 때 항상 밝게 먼저 인사를 하기에 좋은 관계를 유지할 수 있었습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_6"))) {
                val text = "안녕하세요 저는 업무를 수행할 때 주변 동료들과 좋은 관계를 유지하고 협동이 필요한 업무를 할 시에 적극적으로 임하는 자세를 가지고 있습니다 , 뿐만 아니라 저는 업무를 수행할 때" +
                        "뒤로 미루는 일 없이 정해진 기한내에 성실하게 수행할 수 있으며 , 무거운 물건을 들거나 많은 체력을 요구하는 업무도 능숙하게 수행할 수 있는 체력을 가지고 있습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_4", "introduce_color_5"))) {
                val text = "안녕하세요 저는 업무를 수행할 때 주변 동료들과 항상 좋은 관계를 유지하고 ,  협업이 필요한 업무를 할 때 적극적으로 임하는 자세를 가지고 있습니다 , 뿐만 아니라 " +
                        "저는 시간약속을 중요시 하기에 지각이나 , 업무 기한을 어기는 일 없이 항상 정해진 시간을 지키기 위해 노력하며 실천합니다 또, 저는 손님이나 동료를 대할 때 " +
                        "항상 밝게 웃으며 먼저 인사하고 말을 건내는 장점을 가지고 있습니다. "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_4", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 14 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_5", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 15 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_3", "introduce_color_4", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 16 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_3", "introduce_color_4", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 17 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_3", "introduce_color_5", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 18 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_4", "introduce_color_5", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 19 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

        }







    }


    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getImageTag(imageView: ImageView): String {
        val imageId = imageView.id
        return when (imageId) {
            R.id.introduce_collaboration -> "introduce_color_2"
            R.id.introduce_keeptime -> "introduce_color_4"
            R.id.introduce_organize -> "introduce_color_1"
            R.id.introduce_smile -> "introduce_color_5"
            R.id.introduce_sincerity -> "introduce_color_3"
            R.id.introduce_strong -> "introduce_color_6"
            else -> ""
        }
    }
}
