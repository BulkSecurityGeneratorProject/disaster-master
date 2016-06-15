(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service', {
            parent: 'entity',
            url: '/service',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.service.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service/services.html',
                    controller: 'ServiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('service');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-detail', {
            parent: 'entity',
            url: '/service/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.service.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service/service-detail.html',
                    controller: 'ServiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('service');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Service', function($stateParams, Service) {
                    return Service.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('service.new', {
            parent: 'service',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service/service-dialog.html',
                    controller: 'ServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('service', null, { reload: true });
                }, function() {
                    $state.go('service');
                });
            }]
        })
        .state('service.edit', {
            parent: 'service',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service/service-dialog.html',
                    controller: 'ServiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Service', function(Service) {
                            return Service.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service.delete', {
            parent: 'service',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service/service-delete-dialog.html',
                    controller: 'ServiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Service', function(Service) {
                            return Service.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
