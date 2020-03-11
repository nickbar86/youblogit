function readFile(file) {
  return new Promise(function(resolve) {
    var reader = new FileReader(); // This is called when finished reading

    reader.onload = function(event) {
      // Return an array with one image
      resolve({
        // These are attributes like size, name, type, ...
        lastModifiedDate: file.lastModifiedDate,
        lastModified: file.lastModified,
        name: file.name,
        size: file.size,
        type: file.type,
        // This is the files content as base64
        src: event.target.result,
        data: { link: event.target.result }
      });
    };

    if (file.type.indexOf("text/") === 0 || file.type === "application/json") {
      reader.readAsText(file);
    } else if (file.type.indexOf("image/") === 0) {
      reader.readAsDataURL(file);
    } else {
      reader.readAsBinaryString(file);
    }
  });
}

function uploadImageCallBack(file) {
  return new Promise(
    (resolve, reject) => {
      const xhr = new XMLHttpRequest(); // eslint-disable-line no-undef
      xhr.open('POST', 'https://api.imgur.com/3/image');
      xhr.setRequestHeader('Authorization', 'Client-ID 8d26ccd12712fca');
      const data = new FormData(); // eslint-disable-line no-undef
      data.append('image', file);
      xhr.send(data);
      xhr.addEventListener('load', () => {
        const response = JSON.parse(xhr.responseText);
        resolve(response);
      });
      xhr.addEventListener('error', () => {
        const error = JSON.parse(xhr.responseText);
        reject(error);
      });
    },
  );
}

export default function mockUpload(data, success, failed, progress) {
  function doProgress(percent) {
    progress(percent || 1);
    if (percent === 100) {
      // Start reading the file
      Promise.all(data.files.map(readFile)).then(files =>
        success(files, { retainSrc: true })
      );
    } else {
      setTimeout(doProgress, 250, (percent || 0) + 10);
    }
  }

  doProgress();
}
