package ir.easazade.fuelkotlin

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import timber.log.Timber
import java.io.*
import java.util.Random

fun generateImageFileName(): String {
  val rand = Random()
  val letters = arrayOf("postExecute", "b", "c", "d", "e", "preExecute", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9")

  var imageFileName = ""
  for (i in 0..9) {
    val x = rand.nextInt(60)
    imageFileName += letters[x]
  }

  return imageFileName
}

class FileUtilsLegacy {

  companion object {

    fun getAppFolder(): File {
      val marsAppFolder = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "mars_app"
      )
      if (!marsAppFolder.exists()) {
        marsAppFolder.mkdirs()
        marsAppFolder.setWritable(true)
        marsAppFolder.setReadable(true)
      }
      return marsAppFolder
    }

    fun getAppCacheFolder(): File {
      return File(getAppFolder(), "temp")
    }

    fun createNewImageInAppFolder(name: String): File {

      return File(getAppFolder(), name).apply {
        createNewFile()
        setReadable(true)
        setWritable(true)
      }
    }

//    fun createNewImageInAppFolderFromDesign(design: Design): File {
//      val imageExtension = design.name.split(".").let {
//        if (it.size >= 2)
//          return@let it.last()
//        else
//          return@let design.name
//      }
//      val newImageFileName = "${design.id}.$imageExtension"
//      return createNewImageInAppFolder(newImageFileName)
//    }

    /***
     * copies the file to destination and returns destination file
     */
    fun copyFileToCahce(context: Context, source: File): File {
      var inputStream: FileInputStream?
      var outputStream: FileOutputStream?
      val cacheFileOutput = File(context.cacheDir, source.name).apply { createNewFile() }
      try {

        inputStream = FileInputStream(source)
        outputStream = FileOutputStream(cacheFileOutput)

        val buffer = ByteArray(1024)
        var read = inputStream.read(buffer)
        while ((read) != -1) {
          outputStream.write(buffer, 0, read)
          read = inputStream.read(buffer)
        }
        inputStream.close()
        inputStream = null

        // write the output file (You have now copied the file)
        outputStream.flush()
        outputStream.close()
        outputStream = null
      } catch (fnfe1: FileNotFoundException) {
        Timber.e(fnfe1)
      } catch (e: Exception) {
        Timber.e(e)
      }
      return cacheFileOutput
    }

    private fun copyFile(inputPath: String, filename: String, outputPath: String): File? {

      var file: File? = null
      var inputStream: InputStream? = null
      var outputStream: OutputStream? = null
      try {

        //create output directory if it doesn't exist
        val dir = File(outputPath)
        if (!dir.exists()) {
          dir.mkdirs()
        }


        inputStream = FileInputStream(inputPath + filename)
        outputStream = FileOutputStream(outputPath + filename)

        val buffer = ByteArray(1024)
        var read = inputStream.read(buffer)
        while ((read) != -1) {
          outputStream.write(buffer, 0, read)
          read = inputStream.read(buffer)
        }
        inputStream.close()
        inputStream = null

        // write the output file (You have now copied the file)
        outputStream.flush()
        outputStream.close()
        outputStream = null
        file = File(outputPath + filename)
      } catch (fnfe1: FileNotFoundException) {
        Timber.e(fnfe1)
      } catch (e: Exception) {
        Timber.e(e)
      }
      return file
    }

    fun copyFile(inputFile: File, outputFile: File): File {

      var file: File? = null
      var inputStream: InputStream? = null
      var outputStream: OutputStream? = null
      try {

        //create output directory if it doesn't exist
        if (!outputFile.exists()) {
          outputFile.mkdirs()
        }


        inputStream = FileInputStream(inputFile)
        outputStream = FileOutputStream(outputFile)

        val buffer = ByteArray(1024)
        var read = inputStream.read(buffer)
        while ((read) != -1) {
          outputStream.write(buffer, 0, read)
          read = inputStream.read(buffer)
        }
        inputStream.close()
        inputStream = null

        // write the output file (You have now copied the file)
        outputStream.flush()
        outputStream.close()
        outputStream = null
      } catch (fnfe1: FileNotFoundException) {
        Timber.e(fnfe1)
      } catch (e: Exception) {
        Timber.e(e)
      }
      return outputFile
    }

    fun copyImageFileToCacheAndCompress(
      context: Context,
      image: File,
      minSizeOfCompress: Long = 512
    ): File {
      return if (image.length() > minSizeOfCompress) {
        val file = copyFileToCahce(context, image)
        compressImageFile(file)
        if (file.length() < image.length())
          file
        else image
      } else
        image
    }

    fun compressImageFile(file: File): File? {
      try {
        // BitmapFactory options to downsize the image
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        o.inSampleSize = 6
        // factor of downsizing the image

        var inputStream = FileInputStream(file)
        //Bitmap selectedBitmap = null;
        BitmapFactory.decodeStream(inputStream, null, o)
        inputStream.close()

        // The new size we want to scale to
        val REQUIRED_SIZE = 75

        // Find the correct scale value. It should be the power of 2.
        var scale = 1
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
          scale *= 2
        }

        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        inputStream = FileInputStream(file)

        val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
        inputStream.close()

        // here i override the original image file
        file.createNewFile()
        val outputStream = FileOutputStream(file)

        selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        return file
      } catch (e: Exception) {
        Timber.e(e)
        return null
      }
    }

    fun bitmapFromUri(context: Context, imageUri: Uri): Bitmap =
      MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

    /**
     * this method is not tested
     */
    @Deprecated(
      message = "this method is very unsafe  and can cause out of memory " +
          "exceptions for large size images use decodeBitmapFromFile()"
    )
    fun bitmapFromFile(imageFile: File, option: BitmapFactory.Options? = null): Bitmap? {
      var bitmap: Bitmap? = null
      if (option == null) {
        val defaultOptions = BitmapFactory.Options()
        defaultOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = BitmapFactory.decodeFile(imageFile.path, defaultOptions)
      } else {
        bitmap = BitmapFactory.decodeFile(imageFile.path, option)
      }
      return bitmap
    }



    fun uriFromFile(file: File?): Uri = Uri.fromFile(file)

    /**
     * files in the assets directory don't get unpacked. Instead, they are read directly from the APK
     */
    fun getFileFromAssetsAndCopyToCache(dir: String, context: Context): File {
      val outputCacheFile = File(context.cacheDir, generateImageFileName())
      var fos: FileOutputStream? = null
      try {
        //read file
        val inputStream = context.assets.open(dir)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        //now create file in cache directory
        fos = FileOutputStream(outputCacheFile)
        fos.write(buffer)
        fos.close()
      } catch (e: Exception) {
        Timber.e(e)
        fos?.close()
      } catch (ioe: IOException) {
        Timber.e(ioe)
        fos?.close()
      }
      return outputCacheFile
    }

    fun crateCacheFileFromUri(activity: FragmentActivity, imageUri: Uri): Observable<File> {
      //creating postExecute file from uri requires writing it to disk says in android docs
      val bitmap = bitmapFromUri(activity, imageUri)
      return createCacheFileFromBitmapAsync(activity, bitmap)
    }

    fun createCacheFileFromBitmapAsync(
      activity: FragmentActivity,
      bitmap: Bitmap?
    ): Observable<File> {
      return Observable.create { observer ->
        //we create postExecute file in our app cache dir
        val file = File(activity.cacheDir, generateImageFileName())
        val outPutStream = BufferedOutputStream(FileOutputStream(file))
        bitmap?.let {
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, outPutStream)
        }
        outPutStream.close()
        observer.onNext(file)
      }
    }

    fun createAppFileFromUri(context: Context, uri: Uri): File {
      val bitmap = FileUtilsLegacy.bitmapFromUri(context, uri)
      val file = File(context.filesDir, generateImageFileName())
      val outputStream = BufferedOutputStream(FileOutputStream(file))
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      outputStream.close()
      return file
    }

    fun createCacheFileFromBitmap(
      activity: androidx.fragment.app.FragmentActivity,
      bitmap: Bitmap?
    ): File {
      //we create postExecute file in our app cache dir
      val file = File(activity.cacheDir, generateImageFileName())
      val outPutStream = BufferedOutputStream(FileOutputStream(file))
      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outPutStream)
      }
      outPutStream.close()
      return file
    }

    /***
     * large images cause out of memory exception this method first checks the resource image
     * size and then if it is bigger than the reqWidth or reqHeight the bitmap will be decoded
     * with the requested sizes
     */
    fun decodeBitmapFromResource(
      res: Resources,
      resId: Int,
      reqWidth: Int,
      reqHeight: Int
    ): Bitmap {

      // First decode with inJustDecodeBounds=true to check dimensions
      val options: BitmapFactory.Options = BitmapFactory.Options()
      options.inJustDecodeBounds = true
      BitmapFactory.decodeResource(res, resId, options)
      // Calculate inSampleSize
      options.inSampleSize = calculateBitmapFactoryInSampleSize(options, reqWidth, reqHeight)

      // Decode bitmap with inSampleSize setFab
      options.inJustDecodeBounds = false
      return BitmapFactory.decodeResource(res, resId, options)
    }

    fun decodeBitmapFromPath(paramString: String, paramInt1: Int, paramInt2: Int): Bitmap {
      val localOptions = BitmapFactory.Options()
      localOptions.inJustDecodeBounds = true
      BitmapFactory.decodeFile(paramString, localOptions)
      localOptions.inSampleSize =
        calculateBitmapFactoryInSampleSize2(localOptions, paramInt1, paramInt2)
      localOptions.inJustDecodeBounds = false
      return BitmapFactory.decodeFile(paramString, localOptions)
    }

    /***
     * large images cause out of memory exception this method first checks the resource image
     * size and then if it is bigger than the reqWidth or reqHeight the bitmap will be decoded
     * with the requested sizes
     */
    fun decodeBitmapFromFile(file: File, reqWidth: Int, reqHeight: Int): Bitmap {

      // First decode with inJustDecodeBounds=true to check dimensions
      val options: BitmapFactory.Options = BitmapFactory.Options()
      options.inJustDecodeBounds = true
      BitmapFactory.decodeStream(FileInputStream(file), null, options)
      // Calculate inSampleSize
      options.inSampleSize = calculateBitmapFactoryInSampleSize(options, reqWidth, reqHeight)
      // Decode bitmap with inSampleSize setFab
      options.inJustDecodeBounds = false
      return BitmapFactory.decodeStream(FileInputStream(file), null, options)
    }

    private fun calculateBitmapFactoryInSampleSize2(
      paramOptions: BitmapFactory.Options,
      paramInt1: Int,
      paramInt2: Int
    ): Int {
      val i = paramOptions.outHeight
      val j = paramOptions.outWidth
      var k = 1
      if (i > paramInt2 || j > paramInt1) {
        val m = i / 2
        val n = j / 2
        while (m / k >= paramInt2 && n / k >= paramInt1) {
          k *= 2
        }
      }
      return k
    }

    /**
     * copied this method from uCrop library lucky i had the library imported in my project
     * to understand how this method works just read the docs for the
     * inSampleSize property of the BitmapFactory.Options
     */
    private fun calculateBitmapFactoryInSampleSize(
      options: BitmapFactory.Options,
      reqWidth: Int,
      reqHeight: Int
    ): Int {
      // Raw height and width of image
      val height = options.outHeight
      val width = options.outWidth
      var inSampleSize = 1

      if (height > reqHeight || width > reqWidth) {
        // Calculate the largest inSampleSize value that is postExecute power of 2 and keeps both
        // height and width lower or equal to the requested height and width.
        while (height / inSampleSize > reqHeight || width / inSampleSize > reqWidth) {
          inSampleSize *= 2
        }
      }

      return inSampleSize
    }

    fun drawableToBitmap(pd: PictureDrawable): Bitmap {
      val bm =
        Bitmap.createBitmap(pd.intrinsicWidth, pd.intrinsicHeight, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bm)
      canvas.drawPicture(pd.picture)
      return bm
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {

      val bos: ByteArrayOutputStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
      val byteArray: ByteArray = bos.toByteArray()
      return byteArray
    }

    fun imageUriToPath(activity: Context, imageUri: Uri): String? {
      val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
      val myCursor =
        activity.contentResolver.query(imageUri, filePathColumn, null, null, null)
      var imagePath: String? = null
      myCursor?.let { cursor ->
        cursor.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        imagePath = cursor.getString(columnIndex)
      }
      myCursor?.close()
      return imagePath
    }

//    fun imageUriToFile(activity: Context, imageUri: Uri) =
//      File(MyFileUtils.getPath(activity, imageUri))

    fun readJsonFileFromAndroidTestResources(cnx: Context, fileName: String): String? {
//      val resource = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)
      val resource = cnx.classLoader.getResourceAsStream(fileName)
      return if (resource != null) {
        val buffer = ByteArray(resource.available())
        val read = resource.read(buffer)
        val str = String(buffer)
        resource.close()
        if (read > -1)
          str
        else
          null
      } else
        null
    }
  }
}