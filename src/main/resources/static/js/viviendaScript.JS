function previewImages() {
    var preview = document.querySelector('#preview');
    var fileList = document.querySelector('#imageFiles').files;
    var list = document.querySelector('#imageList');
  
    list.innerHTML = '';
  
    for (var i = 0; i < fileList.length; i++) {
      var file = fileList[i];
      var listItem = document.createElement('li');
      listItem.textContent = file.name;
      list.appendChild(listItem);
    }
  
    if (fileList.length === 0) {
      preview.innerHTML = '';
    }
  }