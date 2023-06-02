import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.capstone.R

class WebViewFragment : Fragment() {
    lateinit var webView: WebView
    private var websiteUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        websiteUrl = arguments?.getString(ARG_WEBSITE_URL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_webview, container, false)
        webView = view.findViewById(R.id.webView)

        if (websiteUrl != null) {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = WebViewClient()
            webView.loadUrl(websiteUrl!!)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        webView.requestFocus()
    }

    companion object {
        private const val ARG_WEBSITE_URL = "website_url"

        fun newInstance(websiteUrl: String): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            args.putString(ARG_WEBSITE_URL, websiteUrl)
            fragment.arguments = args
            return fragment
        }
    }
}

class WelfareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

                // 현재 프래그먼트가 WebViewFragment인 경우에만 처리
                if (currentFragment is WebViewFragment) {
                    if (currentFragment.webView.canGoBack()) {
                        // WebView에서 뒤로 갈 수 있다면 뒤로 가기
                        currentFragment.webView.goBack()
                    } else {
                        // WebView에서 더 이상 뒤로 갈 수 없으면 프래그먼트를 종료
                        supportFragmentManager.beginTransaction().remove(currentFragment).commit()
                    }
                } else {
                    // WebViewFragment가 아닌 경우 기본 동작 수행 비활성화
                    isEnabled = false
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}
