let md_content=document.getElementById("md_content")

var div = document.createElement("div");
div.innerHTML = marked(md_content.value)
let imageArray=div.getElementsByTagName("img");
for(let i=0;i<imageArray.length;i++){
    let img=imageArray[i];
    img.setAttribute("width","98%");
}

let content=document.getElementById('content')
content.appendChild(div);