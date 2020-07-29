## Usage

* About runtime permission request

```
//What kind of permission you would like to have.
@SamplePermission(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
class SamplePermissionActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_permission_sample)

        //Add an observer, when the permission request got a response you will receive the result.
        //Because we use ViewModel, So you do not have to release the resources mannally.
        PermissionViewModelProviders.getViewModel(this).addObserver { result->
            if(result.granted){
                val text = getString(R.string.permission_granted, result.name)
                Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
            } else {
                val text = getString(R.string.permission_denied, result.name)
                Toast.makeText(applicationContext,text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

* Display a list

```
//Simple list adapter for RecyclerView
val dataProvider = DataManager.getDataProvider(this)
val simpleArrayAdapter = SimpleArrayAdapter(this, dataProvider.wordList)

//Simple fragment adapter for ViewPager.
val fragmentList=mutableListOf(...)
val adapter=SimpleFragmentPagerAdapter(fragmentList)

```

* About the test data
```
val dataProvider = DataManager.getDataProvider(this)
//Noticed we only have about 600 English test words.
//Ask for one word
dataProvider.word
//Ask for a bunch of word.
dataProvider.getWordList(10)
//From index ten return on hundred testing word.
dataProvider.getWordList(10,100)

//local image urls.
val imageArray = dataProvider.imageArray
//another way to fetch image urls.
val imageAnalyzer=ImageAnalyzer()
imageAnalyzer.setDataSource(HtmlSource())
val imageUrlList=imageAnalyzer.analysis(this,"http://xxx")

//For color resources.
val color = DataProvider.getColor()
//All the material blue color.
val colorArray = dataProvider.getColorArray(DataProvider.COLOR_BLUE_GREY)
```

* About color resources
I already defined all of the martial color resources. 
```
<!-- This colours below are based on Danielle Vass light_colors file :
    https://gist.github.com/daniellevass/b0b8cfa773488e138037 -->
<!--reds-->
<color name="md_red_50">#FFEBEE</color>
<color name="md_red_100">#FFCDD2</color>
<color name="md_red_200">#EF9A9A</color>
<color name="md_red_300">#E57373</color>
<color name="md_red_400">#EF5350</color>
<color name="md_red_500">#F44336</color>
<color name="md_red_600">#E53935</color>
<color name="md_red_700">#D32F2F</color>
<color name="md_red_800">#C62828</color>
<color name="md_red_900">#B71C1C</color>
<color name="md_red_A100">#FF8A80</color>
<color name="md_red_A200">#FF5252</color>
<color name="md_red_A400">#FF1744</color>
<color name="md_red_A700">#D50000</color>

<!-- pinks -->
<color name="md_pink_50">#FCE4EC</color>
<color name="md_pink_100">#F8BBD0</color>
<color name="md_pink_200">#F48FB1</color>
<color name="md_pink_300">#F06292</color>
<color name="md_pink_400">#EC407A</color>
<color name="md_pink_500">#E91E63</color>
<color name="md_pink_600">#D81B60</color>
<color name="md_pink_700">#C2185B</color>
<color name="md_pink_800">#AD1457</color>
<color name="md_pink_900">#880E4F</color>
<color name="md_pink_A100">#FF80AB</color>
<color name="md_pink_A200">#FF4081</color>
<color name="md_pink_A400">#F50057</color>
<color name="md_pink_A700">#C51162</color>
[...]
```

* Display the output message.
Firstly, Configure the annotation: @SampleMessage in your sample class.
Than, Use the System.out.println("xxx) instead of Log. You will have your own panel to display the message.


* Display the source code use @SampleSourceCode

* Display the memory panel. Maybe is not correct. use @SampleMemory

