(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fleet', {
            parent: 'entity',
            url: '/fleet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.fleet.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fleet/fleets.html',
                    controller: 'FleetController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fleet');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fleet-detail', {
            parent: 'entity',
            url: '/fleet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.fleet.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fleet/fleet-detail.html',
                    controller: 'FleetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fleet');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fleet', function($stateParams, Fleet) {
                    return Fleet.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('fleet.new', {
            parent: 'fleet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fleet/fleet-dialog.html',
                    controller: 'FleetDialogController',
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
                    $state.go('fleet', null, { reload: true });
                }, function() {
                    $state.go('fleet');
                });
            }]
        })
        .state('fleet.edit', {
            parent: 'fleet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fleet/fleet-dialog.html',
                    controller: 'FleetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fleet', function(Fleet) {
                            return Fleet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fleet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fleet.delete', {
            parent: 'fleet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fleet/fleet-delete-dialog.html',
                    controller: 'FleetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fleet', function(Fleet) {
                            return Fleet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('fleet', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
