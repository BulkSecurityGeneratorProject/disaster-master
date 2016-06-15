(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('ServiceDialogController', ServiceDialogController);

    ServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Service', 'Fleet'];

    function ServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Service, Fleet) {
        var vm = this;

        vm.service = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fleets = Fleet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.service.id !== null) {
                Service.update(vm.service, onSaveSuccess, onSaveError);
            } else {
                Service.save(vm.service, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:serviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
