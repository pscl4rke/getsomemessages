
Get Some Messages
=================

This Android app reads your text messages out of internal storage
and writes them to the SD card as JSON file.
This can then be used by you for whatever backup/analysis purposes you want.

*   **NOTE:**
    This app had a long and painful birth.
    During the development someone else wrote
    [their own app](https://gitlab.com/hydrargyrum/epistolaire)
    that does almost *exactly* the same thing,
    but with the added advantage that it's
    [already on F-Droid](https://www.f-droid.org/en/packages/re.indigo.epistolaire/).
    You should try that first before playing with mine!

I don't really get the Android philosophy.
This is the sort of thing that I should be able to achieve with a dozen
lines of my favourite scripting language.
But instead I need 43 files in a git repo,
and my code has to have both threads and event handlers.

Usage
-----

*   Launch app.
*   Press Button.
*   Wait a moment.
*   Find `/sdcard/Download/TextMessages.json` and use it for something.

Build and Installation
----------------------

*   Plug in your phone with a USB cable.
*   Enable ADB.
*   Install Android Studio.
*   Click haphazardly on things until it is running on the phone.
*   Uninstall Android Studio.

