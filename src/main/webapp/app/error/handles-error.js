/**
 * JS - Control Exception - CODE SERVER ERRO [ 400/500, 5XX, ...]
 * 
 * @author Felipe <felipeb@ciandt.com>
 * @version <%= pkg.version %>
 */

var ErrorHandles = {}; // Error namespace

ErrorHandles.trataCodeStatus = function($scope, $log, operacao, error) {
	$scope.loading = false;

	var codeErros = {
		400 : "Bad Request",
		403 : "Forbidden",
		404 : "Not Found",
		405 : "Method Not Allowed",
		408 : "Request Timeout",
		411 : "Length Required",
		413 : "Request Entity Too Large",
		500 : "Internal Server Error",
		502 : "Bad Gateway",
		503 : "Service Unavailable",
		504 : "Gateway Timeout"
	};

	var isCode = true;
	if (window.File) {
		angular.forEach(codeErros, function(value, key) {
			if (key == error.code) {
				if (error.code == 401) {
					alert(error.message);
					$log.warn(operacao + ' - ' + 'Code: ' + error.code
							+ ' erro: ' + value);
				} else if (error.code == 503) {
					$log.error(operacao + ' - ' + 'Code: ' + error.code
							+ ' erro: ' + value);
				} else {
					alert(key + " - " + value);
					$log.error(operacao + ' - ' + 'Code: ' + error.code
							+ ' erro: ' + value);
				}
				isCode = false;
			}

		});
	}

	if (isCode) {
		alert(error.message);
		$log.error(operacao + ' - ' + 'Code: ' + error.code + ' erro: '
				+ error.message);
	}

}
