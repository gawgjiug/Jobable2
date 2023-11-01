package com.example.capstone.dialog

import android.os.Bundle
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
            } else {
                // 이미지가 3개 선택된 경우에 대한 추가 로직
                // 예를 들어, 선택된 이미지들에 대한 다음 단계로 진행하는 로직 등을 추가할 수 있습니다.
            }
        }





    }



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
