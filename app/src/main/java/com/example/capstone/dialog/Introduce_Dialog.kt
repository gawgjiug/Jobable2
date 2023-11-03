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


        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // TTS 엔진이 초기화 성공한 경우
                tts?.language = Locale.KOREAN
                tts?.setSpeechRate(0.6f) // 2배 빠른 속도


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
            if (selectedImages.size < 3 || selectedImages.size > 3) {
                Toast.makeText(requireContext(), "이미지를 3개 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_4"))) {

                val text = "안녕하세요 저는 일을 할 때 주변 정리정돈을 잘합니다, 뿐만 아니라 저는 친화력이 좋기 때문에 " +
                        "같이 일하는 동료와 항상 좋은 관계를 유지할 수 있으며 항상 약속된 시간을 지키는 것을 중시하기 때문에 " +
                        "출근 시간에 맞춰 늦지 않도록 노력하는 사람입니다"

                // 리스너가 설정되어 있는지 확인하고 텍스트 전송
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_3"))) {
                val text = "안녕하세요 저는 곽지욱입니다 1 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }
            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 2 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_2", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 3 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_4"))) {
                val text = "안녕하세요 저는 곽지욱입니다 4 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 5"
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_3", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 6 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_4", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 7 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_4", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 8 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_1", "introduce_color_5", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 9 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_4"))) {
                val text = "안녕하세요 저는 곽지욱입니다 10 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }


            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 11 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_3", "introduce_color_6"))) {
                val text = "안녕하세요 저는 곽지욱입니다 12 "
                listener?.onIntroduceTextSelected(text)
                Toast.makeText(requireContext(),"간단 이력서 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                dialog!!.dismiss()
            }

            else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_4", "introduce_color_5"))) {
                val text = "안녕하세요 저는 곽지욱입니다 13 "
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
