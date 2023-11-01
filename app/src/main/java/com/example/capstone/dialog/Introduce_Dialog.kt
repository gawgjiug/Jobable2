package com.example.capstone.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.capstone.R
import com.example.capstone.databinding.IntroduceDialogBinding

class Introduce_Dialog : DialogFragment() {

    private var _binding: IntroduceDialogBinding? = null
    private val binding get() = _binding!!
    private val introduceImageViewIds = listOf(
        R.id.introduce_smile,R.id.introduce_sincerity,R.id.introduce_keeptime,R.id.introduce_strong,
        R.id.introduce_organize,R.id.introduce_collaboration
    )
    private val selectedImages = mutableListOf<String>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroduceDialogBinding.inflate(inflater, container, false)

        // ImageView 참조


        return binding.root
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여기에서 다이얼로그 내의 View나 작업을 설정합니다.
        // 예를 들어 버튼 클릭 등의 동작을 구현합니다.
        val introduceImageViews = introduceImageViewIds.map { binding.root.findViewById<ImageView>(it) }

        binding.introduceCollaboration.setOnClickListener {
            // 사용자가 이미지를 클릭했을 때 introduce_color_2로 변경
            binding.introduceCollaboration.setImageResource(R.drawable.introduce_color_2)
            selectedImages.add("introduce_color_2")
        }

        binding.introduceKeeptime.setOnClickListener {
            binding.introduceKeeptime.setImageResource(R.drawable.introduce_color_4)
            selectedImages.add("introduce_color_4")
        }
        binding.introduceOrganize.setOnClickListener {
            binding.introduceOrganize.setImageResource(R.drawable.introduce_color_1)
            selectedImages.add("introduce_color_1")
        }
        binding.introduceSmile.setOnClickListener {
            binding.introduceSmile.setImageResource(R.drawable.introduce_color_5)
            selectedImages.add("introduce_color_5")
        }
        binding.introduceSincerity.setOnClickListener {
            binding.introduceSincerity.setImageResource(R.drawable.introduce_color_3)
            selectedImages.add("introduce_color_3")
        }
        binding.introduceStrong.setOnClickListener {
            binding.introduceStrong.setImageResource(R.drawable.introduce_color_6)
            selectedImages.add("introduce_color_6")
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




