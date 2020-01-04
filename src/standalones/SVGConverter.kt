package standalones

private fun convert(vector: String): String {
    val height = vector.readValue("android:height").removeSuffix("dp")
    val width = vector.readValue("android:width").removeSuffix("dp")

    val viewportHeight = vector.readValue("android:viewportHeight")
    val viewportWidth = vector.readValue("android:viewportWidth")

    val withReplacements = vector
        .ensureFillIsPresentInPaths()
        .replace("android:pathData", "d")
        .replace("android:fillColor", "fill")
        .replace("android:strokeColor", "stroke")
        .replace("android:strokeWidth", "stroke-width")
        .replace("vector", "svg")
        .replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "")
        .replace(
            "xmlns:android=\"http://schemas.android.com/apk/res/android\"",
            "xmlns=\"http://www.w3.org/2000/svg\"" + """
                
                width="$width"
                height="$height"
                viewBox="0 0 $viewportWidth $viewportHeight"
            """.trimIndent()
        )

    return withReplacements.removeAndroidParams()
}

private fun String.readValue(param: String) =
    substringAfter("${param}=\"", "MISSING PARAM").substringBefore("\"")

private fun String.removeAndroidParams(): String {
    val index = indexOf("android:")
    if (index == -1) return this

    val openingQuoteIndex = indexOf("\"", index)
    val closingQuoteIndex = indexOf("\"", openingQuoteIndex + 1)

    val withOneLessAndroidParam = replaceRange(index, closingQuoteIndex + 1, "")
    return withOneLessAndroidParam.removeAndroidParams()
}

private fun String.ensureFillIsPresentInPaths(minIndex: Int = 0): String {
    val index = indexOf("<path", minIndex)
    if (index == -1) return this

    val closingIndex = indexOf(">", index)
    val substring = substring(index, closingIndex)

    val withFillAlways = if (!substring.contains("fillColor")) {
        substring.replace("<path", "<path fill=\"none\"")
    } else substring

    val afterReplacement = replace(substring, withFillAlways)

    return afterReplacement.ensureFillIsPresentInPaths(closingIndex)
}

fun main() {
//    val input = readLine() ?: return

    val input = """
   <vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="100dp"
    android:height="100dp"
    android:viewportWidth="100"
    android:viewportHeight="100">
  <path
      android:pathData="M4,0L96,0A4,4 0,0 1,100 4L100,96A4,4 0,0 1,96 100L4,100A4,4 0,0 1,0 96L0,4A4,4 0,0 1,4 0z"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M4,0L46,0A4,4 0,0 1,50 4L50,46A4,4 0,0 1,46 50L4,50A4,4 0,0 1,0 46L0,4A4,4 0,0 1,4 0z"
      android:fillColor="#FFB13B"/>
  <path
      android:pathData="M54,50L96,50A4,4 0,0 1,100 54L100,96A4,4 0,0 1,96 100L54,100A4,4 0,0 1,50 96L50,54A4,4 0,0 1,54 50z"
      android:fillColor="#DE8500"/>
  <path
      android:pathData="M50,18.4m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M72.4,27.6m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M81.6,50m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M72.4,72.4m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M50,81.6m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M27.6,72.4m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M18.4,50m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:pathData="M27.6,27.6m-18.4,0a18.4,18.4 0,1 1,36.8 0a18.4,18.4 0,1 1,-36.8 0"
      android:fillColor="#FF9900"/>
  <path
      android:fillColor="#FF000000"
      android:pathData="M63.086,18.385c0,-7.227 -5.859,-13.086 -13.1,-13.086c-7.235,0 -13.096,5.859 -13.096,13.086c-5.1,-5.11 -13.395,-5.11 -18.497,0c-5.119,5.12 -5.119,13.408 0,18.524c-7.234,0 -13.103,5.859 -13.103,13.085c0,7.23 5.87,13.098 13.103,13.098c-5.119,5.11 -5.119,13.395 0,18.515c5.102,5.104 13.397,5.104 18.497,0c0,7.228 5.86,13.083 13.096,13.083c7.24,0 13.1,-5.855 13.1,-13.083c5.118,5.104 13.416,5.104 18.513,0c5.101,-5.12 5.101,-13.41 0,-18.515c7.216,0 13.081,-5.869 13.081,-13.098c0,-7.227 -5.865,-13.085 -13.081,-13.085c5.101,-5.119 5.101,-13.406 0,-18.524C76.502,13.275 68.206,13.275 63.086,18.385z"/>
  <path
      android:pathData="M55.003,23.405v14.488L65.26,27.64c0,-1.812 0.691,-3.618 2.066,-5.005c2.78,-2.771 7.275,-2.771 10.024,0c2.771,2.766 2.771,7.255 0,10.027c-1.377,1.375 -3.195,2.072 -5.015,2.072L62.101,44.982H76.59c1.29,-1.28 3.054,-2.076 5.011,-2.076c3.9,0 7.078,3.179 7.078,7.087c0,3.906 -3.178,7.088 -7.078,7.088c-1.957,0 -3.721,-0.798 -5.011,-2.072H62.1l10.229,10.244c1.824,0 3.642,0.694 5.015,2.086c2.774,2.759 2.774,7.25 0,10.01c-2.75,2.774 -7.239,2.774 -10.025,0c-1.372,-1.372 -2.064,-3.192 -2.064,-5.003L55,62.094v14.499c1.271,1.276 2.084,3.054 2.084,5.013c0,3.906 -3.177,7.077 -7.098,7.077c-3.919,0 -7.094,-3.167 -7.094,-7.077c0,-1.959 0.811,-3.732 2.081,-5.013V62.094L34.738,72.346c0,1.812 -0.705,3.627 -2.084,5.003c-2.769,2.772 -7.251,2.772 -10.024,0c-2.775,-2.764 -2.775,-7.253 0,-10.012c1.377,-1.39 3.214,-2.086 5.012,-2.086l10.257,-10.242H23.414c-1.289,1.276 -3.072,2.072 -5.015,2.072c-3.917,0 -7.096,-3.18 -7.096,-7.088s3.177,-7.087 7.096,-7.087c1.94,0 3.725,0.796 5.015,2.076h14.488L27.646,34.736c-1.797,0 -3.632,-0.697 -5.012,-2.071c-2.775,-2.772 -2.775,-7.26 0,-10.027c2.773,-2.771 7.256,-2.771 10.027,0c1.375,1.386 2.083,3.195 2.083,5.005l10.235,10.252V23.407c-1.27,-1.287 -2.082,-3.053 -2.082,-5.023c0,-3.908 3.175,-7.079 7.096,-7.079c3.919,0 7.097,3.168 7.097,7.079C57.088,20.356 56.274,22.119 55.003,23.405z"
      android:fillColor="#FFFFFF"/>
  <path
      android:pathData="M5.3,50H94.68V90Q94.68,95 89.68,95H10.3Q5.3,95 5.3,90Z"
      android:fillColor="#000000"/>
  <path
      android:pathData="M14.657,54.211h71.394c2.908,0 5.312,2.385 5.312,5.315v17.91c-27.584,-3.403 -54.926,-8.125 -82.011,-7.683V59.526C9.353,56.596 11.743,54.211 14.657,54.211L14.657,54.211z"
      android:fillColor="#3F3F3F"/>
  <path
      android:pathData="M18.312,72.927c-2.103,-2.107 -3.407,-5.028 -3.407,-8.253c0,-6.445 5.223,-11.672 11.666,-11.672c6.446,0 11.667,5.225 11.667,11.672h-6.832c0,-2.674 -2.168,-4.837 -4.835,-4.837c-2.663,0 -4.838,2.163 -4.838,4.837c0,1.338 0.549,2.536 1.415,3.42l0,0c0.883,0.874 2.101,1.405 3.423,1.405v0.012c3.232,0 6.145,1.309 8.243,3.416l0,0c2.118,2.111 3.424,5.034 3.424,8.248c0,6.454 -5.221,11.68 -11.667,11.68c-6.442,0 -11.666,-5.222 -11.666,-11.68h6.828c0,2.679 2.175,4.835 4.838,4.835c2.667,0 4.835,-2.156 4.835,-4.835c0,-1.329 -0.545,-2.527 -1.429,-3.407l0,0c-0.864,-0.88 -2.082,-1.418 -3.406,-1.418l0,0C23.341,76.35 20.429,75.036 18.312,72.927L18.312,72.927L18.312,72.927z"
      android:strokeWidth="0.5035"
      android:fillColor="#FFFFFF"
      android:strokeColor="#000000"/>
  <path
      android:pathData="M61.588,53.005l-8.244,39.849l-6.85,0l-8.258,-39.849l6.846,0l4.838,23.337l4.835,-23.337z"
      android:strokeWidth="0.5035"
      android:fillColor="#FFFFFF"
      android:strokeColor="#000000"/>
  <path
      android:pathData="M73.255,69.513h11.683v11.664l0,0c0,6.452 -5.226,11.678 -11.669,11.678c-6.441,0 -11.666,-5.226 -11.666,-11.678l0,0V64.676h-0.017C61.586,58.229 66.827,53 73.253,53c6.459,0 11.683,5.225 11.683,11.676h-6.849c0,-2.674 -2.152,-4.837 -4.834,-4.837c-2.647,0 -4.82,2.163 -4.82,4.837v16.501l0,0c0,2.675 2.173,4.837 4.82,4.837c2.682,0 4.834,-2.162 4.834,-4.827v-0.012v-4.827h-4.834L73.255,69.513L73.255,69.513z"
      android:strokeWidth="0.5035"
      android:fillColor="#FFFFFF"
      android:strokeColor="#000000"/>
</vector>

    """.trimIndent()

    val svg = convert(input)

    println(svg)
}