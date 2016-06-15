(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vehicle', {
            parent: 'entity',
            url: '/vehicle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.vehicle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicle/vehicles.html',
                    controller: 'VehicleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vehicle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('vehicle-detail', {
            parent: 'entity',
            url: '/vehicle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.vehicle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vehicle/vehicle-detail.html',
                    controller: 'VehicleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('vehicle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Vehicle', function($stateParams, Vehicle) {
                    return Vehicle.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('vehicle.new', {
            parent: 'vehicle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-dialog.html',
                    controller: 'VehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                brand: null,
                                licensePlate: null,
                                vehicleType: null,
                                dateCreated: null,
                                dateLastAccessed: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('vehicle');
                });
            }]
        })
        .state('vehicle.edit', {
            parent: 'vehicle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-dialog.html',
                    controller: 'VehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vehicle', function(Vehicle) {
                            return Vehicle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vehicle.delete', {
            parent: 'vehicle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vehicle/vehicle-delete-dialog.html',
                    controller: 'VehicleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vehicle', function(Vehicle) {
                            return Vehicle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
