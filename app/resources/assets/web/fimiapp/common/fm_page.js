
document.querySelector("[name=viewport]").setAttribute('content', 'width=device-width, height=' + window.innerHeight + ', initial-scale=1.0, maximum-scale=1.0, user-scalable=0');

//宽度基准
!(function(win, doc) {
	function setFontSize() {
		var docEl = doc.documentElement;
		var winWidth = docEl.clientWidth;
		doc.documentElement.style.fontSize = (winWidth / 1080) * 100 + 'px';
	}
	var evt = 'onorientationchange' in win ? 'orientationchange' : 'resize';
	var timer = null;
	win.addEventListener(evt, function() {
		clearTimeout(timer);
		timer = setTimeout(setFontSize, 300);
	}, false);
	win.addEventListener('pageshow', function(e) {
		if (e.persisted) {
			clearTimeout(timer);
			timer = setTimeout(setFontSize, 300);
		}
	}, false)
	setFontSize();
}(window, document))
