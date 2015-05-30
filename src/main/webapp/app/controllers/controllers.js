app
.controller(
		'gcsController',
		function($rootScope, $scope, $location, consultaService,
				$timeout, $log, MESSAGE, CONSTANT, URL, $stateParams,
				$window, Upload) {

			$scope.campoObrigatorioArquivo = MESSAGE.campoObrigatorioArquivo;
			$scope.listaArquivo = MESSAGE.listaArquivo;
			$scope.nenhumRegistroEncontrado = MESSAGE.nenhumRegistroEncontrado;
			$scope.enviarArquivo = MESSAGE.enviarArquivo;

			$scope.deletar = function(fileNameDel) {
				if (confirm("Deseja realmente remover o arquivo?")) {
					showMessage("processando");
					consultaService.deleteFile(URL, fileNameDel).then(
							function(response) {
								if (response.code == 200) {
									$log.info("Delete com sucesso.");
									showClose();
									showMessage("sucessoDeletado");
									$scope.init();
								} else {
									$log.warn("Falha no delete: "
											+ response.mensagem);
									showClose();
									showMessage("falhaDeletar");
								}
							}, function(error) {
								$log.error("Delete com erro.");
								// ErrorHandles.trataCodeStatus($scope,
								// $log, CONSTANT.deletar, error);
								showClose();
								showMessage("falhaDeletar");
							});
				}
			};

			$scope.onFileSelect = function() {
				if (window.File) {
					var inputFile = document
					.getElementById('fileUpload');
					if (inputFile.files[0].size > CONSTANT.limiteSizeUpload) {
						$log
						.info("Upload - tamanho do arquivo nao suportado.");
						$(":file").filestyle('buttonText',
						'Adicionar arquivo');
						showMessage("falhaTamanho");
						showClose();
					} else if (!consultaService
							.validarExtensao(inputFile.files[0].name)) {
						$log
						.info("Upload - extensao arquivo nao suportada.");
						$(":file").filestyle('buttonText',
						'Adicionar arquivo');
						showMessage("falhaTipoArquivo");
						showClose();
					} else {
						if (inputFile.files[0] != null) {
							showMessage("enviando");
							consultaService
							.uploadAuth($scope, URL)
							.then(
									function(response) {
										if (response.code == 200) {
											$log
											.info("Upload Auth com sucesso.");
											consultaService
											.uploadFile(
													$scope,
													$timeout,
													URL,
													Upload,
													inputFile.files[0],
													$rootScope)
													.then(
															function(
																	response) {
																
																if(response.code == 200){
																	$log
																	.info("Upload com sucesso.");
																	$(
																	":file")
																	.filestyle(
																			'buttonText',
																	'Adicionar arquivo');
																	showMessage("sucesso");
																	showClose();
																	$scope
																	.init();
																} else if(response.code == 201){
																	$log
																	.info("Upload com sucesso (atualização de arquivo)!");
																	$(
																	":file")
																	.filestyle(
																			'buttonText',
																	'Adicionar arquivo');
																	showMessage("atualizouArquivo");
																	showClose();
																	$scope
																	.init();
																}
																
																
																
															},
															function(
																	error) {
																$log
																.error("Upload com erro.");
																// ErrorHandles.trataCodeStatus($scope,
																// $log,
																// CONSTANT.upload,
																// error);
																showMessage("falha");
																showClose();
															});
										} else {
											$log
											.warn("Falha no upload:"
													+ response.mensagem);
											showMessage("falha");
											showClose();
										}

									},
									function(error) {
										$log
										.error("Upload Auth com erro.");
										// ErrorHandles.trataCodeStatus($scope,
										// $log,
										// CONSTANT.uploadAuth,
										// error);
										showMessage("falha");
										showClose();
									});
						}
					}

				} else { // IE8 e IE9

					// Size
					// var thefile = new
					// ActiveXObject("Scripting.FileSystemObject").getFile($('#fileUpload').val());

					// Extensao
					var settings = {
							valid_extensions : [ 'gif', 'png', 'jpg',
							                     'jpeg', 'pdf', 'tif' ]
					};
					var ext = $('#fileUpload').val().split('.').pop()
					.toLowerCase();

					/*
					 * if(thefile.size > CONSTANT.limiteSizeUpload){
					 * $log.info("Upload - tamanho do arquivo nao
					 * suportado.");
					 * alert(MESSAGE.tamanhoArquivoNaoSuportado); }else
					 */

					if ($.inArray(ext, settings.valid_extensions) == -1) {
						$log
						.info("Upload - extensao arquivo nao suportada.");
						showMessage("falhaTipoArquivo");
						showClose();
					} else {
						showMessage("carregando");
						consultaService
						.uploadAuth($scope, URL)
						.then(
								function(response) {
									if (response.code == 200) {
										$log
										.info("Upload Auth com sucesso.");
										$
										.ajaxFileUpload({
											url : URL.upload
											+ "?auth=S&version=IES"
											+ $scope
											.getReqParams(),
											type : 'POST',
											dataType : 'html', 	
											fileElementId : 'fileUpload',
											success : function(
													response) {
												if (response == 200) {
													$log
													.info("Upload com sucesso.");
													$(
													":file")
													.filestyle(
															'buttonText',
													'Adicionar arquivo');
													showMessage("sucesso");
													showClose();
													$scope
													.init();
												} else if (response == 201) {
													$log
													.info("Upload com sucesso (atualização de arquivo)!");
													$(
													":file")
													.filestyle(
															'buttonText',
													'Adicionar arquivo');
													showMessage("atualizouArquivo");
													showClose();
													$scope
													.init();
												} else {
													$log
													.warn("Falha no upload: "
															+ response.mensagem);
													showMessage("falha");
													showClose();
												}
											},
											error : function(
													error) {
												$log
												.info("Upload com erro.");
												// ErrorHandles.trataCodeStatus($scope,
												// $log,
												// CONSTANT.upload,
												// error);
												showMessage("falha");
												showClose();
											}
										});
									} else {
										$log
										.warn("Falha no upload:"
												+ response.mensagem);
										showMessage("falha");
										showClose();
									}
								},
								function(error) {
									$log
									.error("Upload Auth com erro.");
									// ErrorHandles.trataCodeStatus($scope,
									// $log,
									// CONSTANT.uploadAuth,
									// error);
									showMessage("falha");
									showClose();
								});
					}
				}
			};

			$scope.init = function() {
				$('#fileUpload').bootstrapFileInput();
				showMessage("carregando");
				consultaService
				.getToken(URL, $stateParams.id)
				.then(
						function(response) {
							$log.info("GetToken com sucesso.");
							$window.sessionStorage.token = response.token;
							consultaService
							.listFiles(URL)
							.then(
									function(response) {
										$log
										.info("List com sucesso.");
										$scope.resultados = response.results;
										showMessage("sucessoCarregar");
										showClose();
									},
									function(error) {
										$log
										.error("List com erro.");
										// ErrorHandles.trataCodeStatus($scope,
										// $log,
										// CONSTANT.list,
										// error);
										showMessage("falhaCarregar");
										showClose();
									});
						}, function(error) {
							$log.error("GetToken com erro.");
							// ErrorHandles.trataCodeStatus($scope,
							// $log, CONSTANT.token , error);
							showMessage("falhaCarregar");
							showClose();
						});
			};

			$scope.getReqParams = function() {
				return $scope.generateErrorOnServer ? '?errorCode='
						+ $scope.serverErrorCode + '&errorMessage='
						+ $scope.serverErrorMsg : '';
			};

		});

app.service('consultaService', function($http, $q, $window) {
	return {

		listFiles : function(URL) {
			var deferred = $q.defer();

			$http({
				method : "GET",
				url : URL.list,
				headers : {
					'Authorization' : 'Bearer ' + $window.sessionStorage.token
				}
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				deferred.reject(data.error);
			});

			return deferred.promise;
		},

		deleteFile : function(URL, fileNameDel) {
			var deferred = $q.defer();

			$http({
				method : "GET",
				url : URL.deletar + "/" + fileNameDel,
				headers : {
					'Authorization' : 'Bearer ' + $window.sessionStorage.token
				}
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				deferred.reject(data.error);
			});

			return deferred.promise;
		},

		uploadAuth : function($scope, URL) {

			var deferred = $q.defer();
			$http({
				method : "GET",
				url : URL.upload + "?auth=N",
				headers : {
					'Authorization' : 'Bearer ' + $window.sessionStorage.token
				}
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				deferred.reject(data.error);
			});

			return deferred.promise;
		},

		uploadFile : function($scope, $timeout, URL, Upload, file, $rootScope) {
			var deferred = $q.defer();

			file.upload = Upload.upload(
					{
						url : URL.upload + "?auth=S&version=IEN"
						+ $scope.getReqParams(),
						method : 'POST',
						headers : {
							'Authorization' : 'Bearer '
								+ $window.sessionStorage.token
						},
						file : file,
						fileFormDataName : 'FileForm'
					}).progress(
							function(evt) {
								$rootScope.porcentagem = parseInt(100.0 * evt.loaded
										/ evt.total)
										+ '% ';
							});

			file.upload.then(function(response) {
				deferred.resolve(response.data);
			}, function(response) {
				if (response.status > 0) {
					deferred.reject(response.status + ': ' + response.data);
				}
			});

			return deferred.promise;
		},

		getToken : function(URL, id) {
			var deferred = $q.defer();
			$http({
				method : "GET",
				url : URL.token + "/" + id
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				deferred.reject(data.error);
			});

			return deferred.promise;
		},

		validarExtensao : function(arquivo) {
			var extensoes, ext, valido;
			extensoes = new Array('.png', '.jpg', '.pdf', '.tiff', '.tif',
			'.gif');

			ext = arquivo.substring(arquivo.lastIndexOf(".")).toLowerCase();
			valido = false;

			for (var i = 0; i <= arquivo.length; i++) {
				if (extensoes[i] == ext) {
					valido = true;
					break;
				}
			}

			if (valido) {
				return true;
			}
			return false;
		},
	}

});