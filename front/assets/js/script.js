function updateCount() {
    var textarea = document.getElementById("tweetText");
    var charCount = document.getElementById("charCount");
    var snapButton = document.getElementById("snapButton");

    var remainingChars = 255 - textarea.value.length;

    charCount.textContent = remainingChars;
    
    if(remainingChars == 255){
        charCount.style.display = "none"
        snapButton.disabled = false;
        snapButton.style.backgroundColor = "";
    }else{
        charCount.style.display = "block"
        if (remainingChars < 0) {
            charCount.style.color = "red";
            snapButton.disabled = true;
            snapButton.style.backgroundColor = "gray";
        } else {
            charCount.style.color = "";
            snapButton.disabled = false;
            snapButton.style.backgroundColor = "";
        }
    }
}
