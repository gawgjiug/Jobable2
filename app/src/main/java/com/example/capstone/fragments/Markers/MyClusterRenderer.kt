package com.example.capstone.fragments.Markers

import android.content.Context
import com.google.android.gms.maps.GoogleMap

import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MyClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem>
) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {

    // 클러스터 아이콘 등을 커스터마이즈할 수 있습니다.
    // 필요에 따라 렌더링을 커스터마이즈할 수 있습니다.
    // 자세한 내용은 공식 문서를 참조하세요.
}
