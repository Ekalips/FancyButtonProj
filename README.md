[ ![Download](https://api.bintray.com/packages/ekalips/FancyProgressButton/FancyProgressButton/images/download.svg) ](https://bintray.com/ekalips/FancyProgressButton/FancyProgressButton/_latestVersion)


# FancyButtonProj

Juste a funcy button with progress bar

<img alt="Showcase" src="https://s29.postimg.org/9bfm2dvzr/ezgif_com_crop.gif" />


#   List of attributes:

 1) ```app:f_btnStyle with values "stroke" , "fill", "stroke_fill".  [default - stroke]```
 
Use this for defining style of your button
        
 2) ```app:f_text  [default - ""]```
 
 3) ```app:f_textColor [default - black]```
 
 4) ```app:f_fillColor [default - transperent]```
 
Use this for changing fill color of button (for "fill" and "stroke_fill" styles)
        
 5) ```app:f_strokeColor [default - black]```
 
Use this for changing stroke color of button (for "fill" and "stroke_fill" styles)
        
 6) ```app:f_progressColor [default - black]```
 
Use this for changing color of progress bar (for "fill" and "stroke_fill" styles)
       
 7) ```app:f_hideFillAfterCollapse [default - false]```
 
Use this for determing if button will stay on screen after collapsing (for "fill" and "stroke_fill", looks like                 little circle inside of progress bar)
        
 8) ```app:f_strokeWidth [default - 4]```
 
Determins stroke width (for "stroke" and "stroke_fill")
        
 9) ```app:f_capsText [default - true]```
 
If true will make all text uppercase
        
        

#   Example of usage:

    xmlns:app="http://schemas.android.com/apk/res-auto"

    <com.ekalips.fancybuttonproj.FancyButton
        android:id="@+id/btn1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:f_btnStyle="stroke_fill"
        app:f_fillColor="@android:color/holo_blue_bright"
        app:f_hideFillAfterCollapse="false"
        app:f_progressColor="@android:color/holo_green_light"
        app:f_strokeColor="@android:color/holo_red_light"
        app:f_text="Button"
        app:f_textColor="#000"
        app:f_textSize="22sp" />
        
        
        FancyButton button1 = (FancyButton) findViewById(R.id.btn1);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof  FancyButton)
                {
                    if (((FancyButton)view).isExpanded())
                        ((FancyButton)view).collapse();
                    else
                        ((FancyButton)view).expand();
                }

            }
        };
        button1.setOnClickListener(listener);



#   Adding to project:
   
##      Maven
    
        <dependency>
          <groupId>com.ekalips.android</groupId>
          <artifactId>fancyprogressbutton</artifactId>
          <version>1.2.1</version>
          <type>pom</type>
        </dependency>
        
##      Gradle

        implementation 'com.ekalips.android:fancyprogressbutton:1.2.1'
        
##      Ivy

        <dependency org='com.ekalips.android' name='fancyprogressbutton' rev='1.2.1'>
        <artifact name='fancyprogressbutton' ext='pom' />
        </dependency>
        



```
Copyright 2017 Ekalips

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
