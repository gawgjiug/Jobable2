package com.example.capstone.dialog

import android.os.Bundle
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





class Introduce_Dialog : DialogFragment() {

    private var _binding: IntroduceDialogBinding? = null
    private val binding get() = _binding!!
    private val selectedImages = mutableListOf<String>()

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
            } else if (selectedImages.containsAll(listOf("introduce_color_2", "introduce_color_4", "introduce_color_1"))) {


                binding.introduceResult.text = ("안녕하세요 저는 일을 할 때 주변 정리정돈을 잘합니다, 뿐만 아니라 " +
                        "저는 친화력이 좋기 때문에 같이 일하는 동료와 항상 좋은 관계를 유지할 수 있으며, " +
                        "항상 약속된 시간을 지키는 것을 중시하기 때문에 출근 시간에 맞춰 늦지 않도록 노력하는 사람입니다.").toEditable()


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
