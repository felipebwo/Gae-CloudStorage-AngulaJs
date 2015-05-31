'use strict';

var app = angular.module('gcs', [ 'ui.router', 'ngFileUpload' ]);

app.config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/init');

	$stateProvider.state('init', {
		url : '/init/:id',
		templateUrl : 'app/views/app.html'
	});

});

app	.constant("MESSAGE", {
	"campoObrigatorioArquivo" : "O campo arquivo \u00e9 obrigat\u00f3rio.",
	"listaArquivo" : "Lista de Arquivos",
	"nenhumRegistroEncontrado" : "Nenhum registro encontrado.",
	"enviarArquivo" : "Enviar Arquivo",
	"tamanhoArquivoNaoSuportado" : "Tamanho de arquivo n\u00e3o suportado. Limite m\u00e1ximo s\u00e3o 5MB.",
	"extensaoArquivoNaoSuportada" : "Extens\u00e3o de arquivo n\u00e3o suportada. As extens\u00f5es aceitas s\u00e3o: png, jpg, pdf, tiff, gif, jpeg."
});

app.constant("CONSTANT", {
	"limiteSizeUpload" : 6000000,
	"download" : "Download",
	"list" : "List",
	"deletar" : "Deletar",
	"upload" : "Upload",
	"uploadAuth" : "UploadAuth",
	"token" : "Token"
});


app.constant("URL", {
	"list" : "http://localhost:8080/_ah/api/steloStorageAPI/v1/listar",
	"deletar" : "http://localhost:8080/_ah/api/steloStorageAPI/v1/deletar",
	"upload" : "http://localhost:8080/upload",
	"token" : "http://localhost:8080/_ah/api/steloStorageAPI/v1/result"
});
