# AirySnackbar

A custom and customisable Android snackbar library.

![Language](https://img.shields.io/github/languages/top/akndmr/AirySnackbar) 
![GitHub issues](https://img.shields.io/github/issues/akndmr/AirySnackbar)
![GitHub Repo stars](https://img.shields.io/github/stars/akndmr/AirySnackbar?style=social)
![JitPack](https://img.shields.io/jitpack/version/com.github.akndmr/AirySnackbar)
## Installation

Step 1. Add the JitPack repository to your build file

```gradle
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```gradle
  dependencies {
	    implementation 'com.github.akndmr:AirySnackbar:LatestVersion'
	}
```
    

## Demo

https://user-images.githubusercontent.com/15641747/203623672-40cc7e4a-78f2-4f77-bc66-c4495374ab90.mov


## Usage

#### Show a AirySnackbar

```kotlin
   AirySnackbar.make(
                    source = AirySnackbarSource.ActivitySource(activity = this),
                    type = Type.Custom,
                    attributes =
                    listOf(
                        TextAttribute.Text(text = "Custom color bg AirySnackbar"),
                        TextAttribute.TextColor(textColor = R.color.black),
                        IconAttribute.Icon(iconRes = R.drawable.ic_custom),
                        IconAttribute.IconColor(iconTint = R.color.teal_200),
                        SizeAttribute.Margin(left = 24, right = 24, unit = SizeUnit.DP),
                        SizeAttribute.Padding(top = 12, bottom = 12, unit = SizeUnit.DP),
                        RadiusAttribute.Radius(radius = 8f),
                        GravityAttribute.Top,
                        AnimationAttribute.FadeInOut
                    )
                ).show()
```

We have 3 parameters for a snackbar, as listed below.

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `source` | `AirySnackbarSource` | ActivitySource, DialogSource, ViewSource |
| `type` | `AirySnackbarType` | Success, Error, Info, Warning, Default, Custom |
| `attributes` | `List<AirySnackbarAttribute>` | IconAttribute, TextAttribute, SizeAttribute, RadiusAttribute, GravityAttribute, AnimationAttribute |



### AirySnackbarSource

Provides parent ViewGroup and context for snackbar.

| Source | Description     |
| :-------- | :------- |
| `ActivitySource` | Provides window?.decorView as source. |
| `DialogSource` | Provides dialog's window?.decorView as source and dies if dialog is destroyed before snackbar itself. |
| `ViewSource` | Provides view's parent (android.R.id.content) as source and dies if view is destroyed before snackbar itself. |

#### Note: 
Choose right source wisely. Watch the video below to see possible side effects of sources.

https://user-images.githubusercontent.com/15641747/205439734-8fda16eb-0ef0-4290-97dc-70eb5750e6e6.mov

### AirySnackbarType
There are predefined types like Success and Error. Only difference between types is background color for now. You can prefer Custom type to set your own background color.
`Custom(@ColorRes val bgColor: Int)`


### AirySnackbarAttribute

| Type | Attributes     |
| :-------- | :------- |
| `TextAttribute` | Text, TextColor, TextSize |
| `IconAttribute` | Icon, IconColor, NoIcon |
| `SizeAttribute` | Margin, Padding |
| `RadiusAttribute` | Radius |
| `GravityAttribute`* | Top, Bottom |
| `AnimationAttribute`** | FadeInOut, SlideInOut |

*Recommended gravity is the default top gravity. Using bottom may cause some issues. <br/>
**Recommended animation is the default FadeInOut animation. Using SlideInOut animation with top(default) gravity will cause issues.


## Roadmap

- Fix animation issues or remove slide animation.
- Add custom duration.
- Fix bottom gravity issues or remove bottom option.

